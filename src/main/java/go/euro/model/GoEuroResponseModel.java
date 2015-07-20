package go.euro.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by churmuzache on 7/17/15.
 */
public class GoEuroResponseModel implements Serializable{

    private List<GoEuroModel> goEuroModelList;

    public List<GoEuroModel> getGoEuroModelList() {
        return goEuroModelList;
    }

    public void setGoEuroModelList(List<GoEuroModel> goEuroModelList) {
        this.goEuroModelList = goEuroModelList;
    }

}
