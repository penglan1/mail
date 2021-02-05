package pers.penglan.mail.service.flag;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.penglan.mail.model.FlagMailBaseInfoVO;

import java.util.List;

/**
 * @Author PENGL
 * 2020-03-28
 */
@Service
public class FlagManager {

    @Autowired
    private Logger logger;

    public List<FlagMailBaseInfoVO> getALlFlagMail() {

        return null;
    }
}
