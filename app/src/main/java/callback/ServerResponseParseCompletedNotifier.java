package callback;

import java.util.ArrayList;

import model.GeneralResponseModel;


public interface ServerResponseParseCompletedNotifier {

    void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo);

}
