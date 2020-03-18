// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.modeldb.versioning.autogenerated._public.modeldb.versioning.model;

import ai.verta.modeldb.versioning.*;
import ai.verta.modeldb.versioning.blob.diff.*;
import com.pholser.junit.quickcheck.generator.*;
import com.pholser.junit.quickcheck.generator.java.util.*;
import com.pholser.junit.quickcheck.random.*;
import java.util.*;

public class EnvironmentVariablesDiffGen extends Generator<EnvironmentVariablesDiff> {
  public EnvironmentVariablesDiffGen() {
    super(EnvironmentVariablesDiff.class);
  }

  @Override
  public EnvironmentVariablesDiff generate(SourceOfRandomness r, GenerationStatus status) {
    // if (r.nextBoolean())
    //     return null;

    EnvironmentVariablesDiff obj = new EnvironmentVariablesDiff();
    if (r.nextBoolean()) {
      obj.setStatus(
          Utils.removeEmpty(gen().type(DiffStatusEnumDiffStatus.class).generate(r, status)));
    }
    if (r.nextBoolean()) {
      obj.setName(Utils.removeEmpty(new StringGenerator().generate(r, status)));
    }
    if (r.nextBoolean()) {
      obj.setValueA(Utils.removeEmpty(new StringGenerator().generate(r, status)));
    }
    if (r.nextBoolean()) {
      obj.setValueB(Utils.removeEmpty(new StringGenerator().generate(r, status)));
    }
    return obj;
  }
}