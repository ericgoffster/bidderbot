package bbidder;

import java.util.function.Supplier;

import bbidder.inferences.Balanced;
import bbidder.inferences.CombinedTotalPointsRange;
import bbidder.inferences.FitInSuit;
import bbidder.inferences.HCPRange;
import bbidder.inferences.LongestOrEqual;
import bbidder.inferences.OpeningPreempt;
import bbidder.inferences.SpecificCards;
import bbidder.inferences.StoppersInSuits;
import bbidder.inferences.SuitRange;
import bbidder.inferences.TotalPointsRange;

public class SimpleInferenceRegistryFactory implements Supplier<InferenceRegistry> {

    @Override
    public InferenceRegistry get() {
        InferenceRegistry reg = new InferenceRegistry();
        reg.add(Balanced::valueOf);
        reg.add(HCPRange::valueOf);
        reg.add(OpeningPreempt::valueOf);
        reg.add(LongestOrEqual::valueOf);
        reg.add(SuitRange::valueOf);
        reg.add(FitInSuit::valueOf);
        reg.add(TotalPointsRange::valueOf);
        reg.add(CombinedTotalPointsRange::valueOf);
        reg.add(SpecificCards::valueOf);
        reg.add(StoppersInSuits::valueOf);
        return reg;
    }

}
