package bbidder;

import java.util.function.Supplier;

import bbidder.generalities.FitEstablished;
import bbidder.generalities.IAmTwoSuitedGenerality;
import bbidder.generalities.IBidSuitGenerality;
import bbidder.generalities.PartnerBidSuitGenerality;
import bbidder.generalities.PartnerIsTwoSuitedGenerality;
import bbidder.generalities.TrueGenerality;
import bbidder.generalities.UnbidSuitGenerality;
import bbidder.generalities.WeAreThreeSuited;
import bbidder.inferences.Always;
import bbidder.inferences.Balanced;
import bbidder.inferences.CombinedTotalPointsRange;
import bbidder.inferences.FitInSuit;
import bbidder.inferences.HCPRange;
import bbidder.inferences.LongestOrEqual;
import bbidder.inferences.Preference;
import bbidder.inferences.Rebiddable;
import bbidder.inferences.RebiddableSecondSuit;
import bbidder.inferences.SpecificCards;
import bbidder.inferences.StoppersInSuits;
import bbidder.inferences.SuitRange;
import bbidder.inferences.TotalPointsRange;
import bbidder.inferences.UnBalanced;
import bbidder.inferences.VeryBalanced;

public final class SimpleInferenceRegistryFactory implements Supplier<InferenceRegistry> {

    @Override
    public InferenceRegistry get() {
        InferenceRegistry reg = new InferenceRegistry();
        reg.addInference(Balanced::valueOf);
        reg.addInference(VeryBalanced::valueOf);
        reg.addInference(UnBalanced::valueOf);
        reg.addInference(HCPRange::valueOf);
        reg.addInference(LongestOrEqual::valueOf);
        reg.addInference(SuitRange::valueOf);
        reg.addInference(FitInSuit::valueOf);
        reg.addInference(Rebiddable::valueOf);
        reg.addInference(RebiddableSecondSuit::valueOf);
        reg.addInference(TotalPointsRange::valueOf);
        reg.addInference(CombinedTotalPointsRange::valueOf);
        reg.addInference(SpecificCards::valueOf);
        reg.addInference(StoppersInSuits::valueOf);
        reg.addInference(Always::valueOf);
        reg.addInference(Preference::valueOf);

        reg.addGenerality(TrueGenerality::valueOf);
        reg.addGenerality(FitEstablished::valueOf);
        reg.addGenerality(IAmTwoSuitedGenerality::valueOf);
        reg.addGenerality(PartnerIsTwoSuitedGenerality::valueOf);
        reg.addGenerality(WeAreThreeSuited::valueOf);
        reg.addGenerality(IBidSuitGenerality::valueOf);
        reg.addGenerality(UnbidSuitGenerality::valueOf);
        reg.addGenerality(PartnerBidSuitGenerality::valueOf);
        return reg;
    }

}
