package com.amazonaws.lambdafunction.callers;

import com.amazonaws.services.lambda.invoke.LambdaFunction;

/*
 * Interacts with the function login_function
 */
public interface LoginService {
	@LambdaFunction(functionName="login_function")
	Output login(LoginInput input);
}
