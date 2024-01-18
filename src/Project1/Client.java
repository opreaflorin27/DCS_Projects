package Project1;

import FrameWork.Components.Activation;
import FrameWork.Components.Condition;
import FrameWork.Components.GuardMapping;
import FrameWork.Components.PetriNet;
import FrameWork.Components.PetriNetWindow;
import FrameWork.Components.PetriTransition;
import FrameWork.DataObjects.DataFloat;
import FrameWork.DataObjects.DataSubPetriNet;
import FrameWork.DataObjects.DataTransfer;
import FrameWork.DataOnly.TransferOperation;
import FrameWork.Enumerations.LogicConnector;
import FrameWork.Enumerations.TransitionCondition;
import FrameWork.Enumerations.TransitionOperation;

public class Client {
    public static void main(String[] args) {
        PetriNet pn = new PetriNet();
        pn.PetriNetName = "Client";
        pn.NetworkPort = 1095;

        // ------------------------------------------------------------------------

        DataFloat p0 = new DataFloat();
        p0.SetName("p0");
        p0.SetValue(1.0f);
        pn.PlaceList.add(p0);

        DataFloat p1 = new DataFloat();
        p1.SetName("p1");
        pn.PlaceList.add(p1);

        DataTransfer p3 = new DataTransfer();
        p3.SetName("p3");
        p3.Value = new TransferOperation("localhost", "1085", "p1");
        pn.PlaceList.add(p3);

        DataSubPetriNet p4 = new DataSubPetriNet();
        p4.SetName("p4");
        pn.PlaceList.add(p4);

        DataSubPetriNet p5 = new DataSubPetriNet();
        p5.SetName("p5");
        pn.PlaceList.add(p5);

        DataSubPetriNet p6 = new DataSubPetriNet();
        p6.SetName("p6");
        pn.PlaceList.add(p6);

        // T1------------------------------------------------------------------------
        PetriTransition t1 = new PetriTransition(pn);
        t1.TransitionName = "t1";
        t1.InputPlaceName.add("p1");
        t1.InputPlaceName.add("p0");

        Condition T1Ct1 = new Condition(t1, "p1", TransitionCondition.NotNull);
        Condition T1Ct2 = new Condition(t1, "p0", TransitionCondition.NotNull);
        T1Ct1.SetNextCondition(LogicConnector.AND, T1Ct2);

        GuardMapping grdT1 = new GuardMapping();
        grdT1.condition = T1Ct1;


        grdT1.Activations.add(new Activation(t1, "p1", TransitionOperation.SendOverNetwork, "p3"));
        grdT1.Activations.add(new Activation(t1, "p0", TransitionOperation.Move, "p4"));

        t1.GuardMappingList.add(grdT1);
        t1.Delay = 0;
        pn.Transitions.add(t1);

        // T2------------------------------------------------------------------------
        PetriTransition t2 = new PetriTransition(pn);
        t2.TransitionName = "t2";
        t2.InputPlaceName.add("p4");
        t2.InputPlaceName.add("p5");


        Condition T2Ct1 = new Condition(t2, "p5", TransitionCondition.NotNull);
        Condition T2Ct2 = new Condition(t2, "p4", TransitionCondition.NotNull);
        T2Ct2.SetNextCondition(LogicConnector.AND, T2Ct2);


        GuardMapping grdT2 = new GuardMapping();
        grdT2.condition = T2Ct1;


        grdT2.Activations.add(new Activation(t2, "p5", TransitionOperation.Move, "p6"));
        grdT2.Activations.add(new Activation(t2, "p5", TransitionOperation.Move, "p0"));

        t2.GuardMappingList.add(grdT2);
        t2.Delay = 0;
        pn.Transitions.add(t2);

        System.out.println("Client started \n ------------------------------");
        pn.Delay = 5000;

        PetriNetWindow frame = new PetriNetWindow(false);
        frame.petriNet = pn;
        frame.setVisible(true);
    }
}