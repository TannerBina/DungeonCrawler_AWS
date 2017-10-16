package com.amazonaws.lambdafunction.callers;
import com.amazonaws.services.lambda.invoke.LambdaFunction;

/*
 * Interface to define interaction with the
 * lambda function create_user_function
 */
public interface CreateUserService {
	@LambdaFunction(functionName="create_user_function")
	Output createUser(CreateUserInput input);
}
