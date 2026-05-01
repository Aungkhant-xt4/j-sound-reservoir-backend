package org.java.talentservice.service.impl;

import org.java.talentservice.service.TalentService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TalentServiceImpl implements TalentService {
    @Override
    public List<String> getAll() {
        return Collections.emptyList();
    }
}