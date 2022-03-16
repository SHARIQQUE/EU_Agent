package agent.eui.sendmoney_cashtocash_fragment;

public class ModalFragmentManage {

    static String fragment_for_sender ="";
    static String fragmentNo_for_receiver ="";

    public static String getFragment_for_sender() {
        return fragment_for_sender;
    }

    public static void setFragment_for_sender(String fragment_for_sender) {
        ModalFragmentManage.fragment_for_sender = fragment_for_sender;
    }

    public static String getFragmentNo_for_receiver() {
        return fragmentNo_for_receiver;
    }

    public static void setFragmentNo_for_receiver(String fragmentNo_for_receiver) {
        ModalFragmentManage.fragmentNo_for_receiver = fragmentNo_for_receiver;
    }
}

