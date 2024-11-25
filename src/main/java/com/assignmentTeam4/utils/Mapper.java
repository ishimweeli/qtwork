package com.assignmentTeam4.utils;

import org.modelmapper.ModelMapper;
import com.assignmentTeam4.models.User;

public class Mapper {

    public static ModelMapper modelMapper = new ModelMapper();

    public static User getUserFromDTO(Object object) {
        return modelMapper.map(object, User.class);
    }


}
