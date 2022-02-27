package bbidder;

import java.util.function.Supplier;

import bbidder.inferences.Balanced;
import bbidder.inferences.HCPRange;
import bbidder.inferences.LongestOrEqualHigherRanking;
import bbidder.inferences.LongestOrEqualLowerRanking;
import bbidder.inferences.OpeningPreempt;
import bbidder.inferences.SuitRange;

public class SimpleInferenceRegistryFactory implements Supplier<InferenceRegistry> {

    @Override
    public InferenceRegistry get() {
        InferenceRegistry reg = new InferenceRegistry();
        reg.add(Balanced::valueOf);
        reg.add(HCPRange::valueOf);
        reg.add(OpeningPreempt::valueOf);
        reg.add(LongestOrEqualHigherRanking::valueOf);
        reg.add(LongestOrEqualLowerRanking::valueOf);
        reg.add(SuitRange::valueOf);
        return reg;
    }

}
