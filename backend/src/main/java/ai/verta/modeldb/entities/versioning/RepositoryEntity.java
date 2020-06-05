package ai.verta.modeldb.entities.versioning;

import ai.verta.modeldb.ModelDBConstants;
import ai.verta.modeldb.dto.WorkspaceDTO;
import ai.verta.modeldb.entities.AttributeEntity;
import ai.verta.modeldb.utils.RdbmsUtils;
import ai.verta.modeldb.versioning.Repository;
import ai.verta.modeldb.versioning.Repository.Builder;
import ai.verta.modeldb.versioning.SetRepository;
import com.google.protobuf.InvalidProtocolBufferException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "repository")
public class RepositoryEntity {

  public RepositoryEntity() {}

  public RepositoryEntity(Repository repository, WorkspaceDTO workspaceDTO)
      throws InvalidProtocolBufferException {
    this.name = repository.getName();
    this.date_created = new Date().getTime();
    this.date_updated = new Date().getTime();
    this.repository_visibility = repository.getRepositoryVisibilityValue();
    this.repositoryAccessModifier = repository.getRepositoryAccessModifierValue();
    if (workspaceDTO.getWorkspaceId() != null) {
      this.workspace_id = workspaceDTO.getWorkspaceId();
      this.workspace_type = workspaceDTO.getWorkspaceType().getNumber();
      this.owner = repository.getOwner();
    } else {
      this.workspace_id = "";
      this.workspace_type = 0;
      this.owner = "";
    }
    setAttributeMapping(
        RdbmsUtils.convertAttributesFromAttributeEntityList(
            this, ModelDBConstants.ATTRIBUTES, repository.getAttributesList()));
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UNSIGNED")
  private Long id;

  @Column(name = "name", columnDefinition = "varchar", length = 50)
  private String name;

  @Column(name = "date_created")
  private Long date_created;

  @Column(name = "date_updated")
  private Long date_updated;

  @Column(name = "workspace_id")
  private String workspace_id;

  @Column(name = "workspace_type", columnDefinition = "varchar")
  private Integer workspace_type;

  @OrderBy("date_created")
  @ManyToMany(mappedBy = "repository")
  private Set<CommitEntity> commits = new HashSet<>();

  @Column(name = "owner")
  private String owner;

  @Column(name = "repository_visibility")
  private Integer repository_visibility = null;

  @Column(name = "repository_access_modifier")
  private Integer repositoryAccessModifier = null;

  @Column(name = "deleted")
  private Boolean deleted = false;

  @OneToMany(
      targetEntity = AttributeEntity.class,
      mappedBy = "repositoryEntity",
      cascade = CascadeType.ALL)
  @LazyCollection(LazyCollectionOption.FALSE)
  @OrderBy("id")
  private List<AttributeEntity> attributeMapping;

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Long getDate_created() {
    return date_created;
  }

  public Long getDate_updated() {
    return date_updated;
  }

  public void setDate_updated(Long date_updated) {
    this.date_updated = date_updated;
  }

  public String getWorkspace_id() {
    return workspace_id;
  }

  public Set<CommitEntity> getCommits() {
    return commits;
  }

  public Integer getWorkspace_type() {
    return workspace_type;
  }

  public Boolean getDeleted() {
    return deleted;
  }

  public void setDeleted(Boolean deleted) {
    this.deleted = deleted;
  }

  public List<AttributeEntity> getAttributeMapping() {
    return attributeMapping;
  }

  public void setAttributeMapping(List<AttributeEntity> attributeMapping) {
    if (this.attributeMapping == null) {
      this.attributeMapping = new ArrayList<>();
    }
    this.attributeMapping.addAll(attributeMapping);
  }

  public Repository toProto() throws InvalidProtocolBufferException {
    final Builder builder =
        Repository.newBuilder()
            .setId(this.id)
            .setName(this.name)
            .setDateCreated(this.date_created)
            .setDateUpdated(this.date_updated)
            .setWorkspaceId(this.workspace_id)
            .setWorkspaceTypeValue(this.workspace_type)
            .addAllAttributes(
                RdbmsUtils.convertAttributeEntityListFromAttributes(getAttributeMapping()));
    if (repository_visibility != null) {
      builder.setRepositoryVisibilityValue(repository_visibility);
    }
    if (repositoryAccessModifier != null) {
      builder.setRepositoryAccessModifierValue(repositoryAccessModifier);
    }
    if (owner != null) {
      builder.setOwner(owner);
    }
    return builder.build();
  }

  public void update(SetRepository request) {
    final Repository repository = request.getRepository();
    this.name = repository.getName();
    this.date_updated = new Date().getTime();
    this.repository_visibility = repository.getRepositoryVisibilityValue();
    this.repositoryAccessModifier = repository.getRepositoryAccessModifierValue();
  }

  public String getOwner() {
    return owner;
  }

  public Integer getRepository_visibility() {
    return repository_visibility;
  }
}
