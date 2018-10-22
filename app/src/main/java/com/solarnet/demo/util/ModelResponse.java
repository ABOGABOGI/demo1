package com.solarnet.demo.util;

import java.util.ArrayList;

class ModelResponse {
    public int status;
    public Error error;
    public static class Error{
        public String code;
        public ArrayList<String> descriptions;
        public String description;
    }
}
