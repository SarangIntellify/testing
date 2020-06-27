package com.amazonaws.lambda.stepfunctiondemo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaFunctionHandler implements RequestHandler<Request, String> {

    @Override
    public String handleRequest(Request input, Context context) {
        return new Service().process(input);
    }
}
