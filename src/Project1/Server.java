package Project1;

import java.util.ArrayList;

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

public class Server {
    public static void main(String[] args) {
        PetriNet pn = new PetriNet();
        pn.PetriNetName = "Server";
        pn.NetworkPort = 1085;

        // ------------------------------------------------------------------------

        DataFloat constValue = new DataFloat();
        constValue.SetName("constValue");
        constValue.SetValue(0.01f);
        pn.ConstantPlaceList.add(constValue);

        DataFloat p0 = new DataFloat();
        p0.SetName("p0");
        p0.SetValue(1.0f);
        pn.PlaceList.add(p0);

        DataSubPetriNet p1 = new DataSubPetriNet();
        p1.SetName("p1");
        pn.PlaceList.add(p1);

        DataFloat p2 = new DataFloat();
        p2.SetName("p2");
        pn.PlaceList.add(p2);

        DataTransfer p3 = new DataTransfer();
        p3.SetName("p3");
        p3.Value = new TransferOperation("localhost", "1095", "p5");
        pn.PlaceList.add(p3);

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
        ArrayList<String> lstInput = new ArrayList<String>();
        lstInput.add("p1");
        lstInput.add("constValue");

        grdT1.Activations.add(new Activation(t1, lstInput, TransitionOperation.Prod, "p2"));
        t1.GuardMappingList.add(grdT1);
        t1.Delay = 0;
        pn.Transitions.add(t1);

        // T2------------------------------------------------------------------------
        PetriTransition t2 = new PetriTransition(pn);
        t2.TransitionName = "t2";
        t2.InputPlaceName.add("p2");

        Condition T2Ct1 = new Condition(t2, "p2", TransitionCondition.NotNull);

        GuardMapping grdT2 = new GuardMapping();
        grdT2.condition = T2Ct1;

        grdT2.Activations.add(new Activation(t2, "p2", TransitionOperation.Move, "p0"));
        grdT2.Activations.add(new Activation(t2, "p2", TransitionOperation.SendOverNetwork, "p3"));

        t2.GuardMappingList.add(grdT2);
        t2.Delay = 0;
        pn.Transitions.add(t2);


        System.out.println("Server started \n ------------------------------");
        pn.Delay = 5000;

        PetriNetWindow frame = new PetriNetWindow(false);
        frame.petriNet = pn;
        frame.setVisible(true);
    }
}