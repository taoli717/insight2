package com.insight.generator.setup;

import com.insight.generator.dao.SetUpDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by tli on 1/11/2015.
 */
@Service
public class SetUpServiceImpl implements SetUpService {
    @Autowired
    SetUpDao setUpDao;

    @Override
    public boolean isSetUp() {
        return setUpDao.isSetUp();
    }

    @Override
    public void setUpSuccess() {
        setUpDao.setUpScccess();
    }
}
