package com.amazonaws.lambdafunction.callers;

import com.amazonaws.services.lambda.invoke.LambdaFunction;

/*
 * Interface to define interaction with the
 * lambda function delete_user_function
 */
public interface DeleteCharacterService {
	@LambdaFunction(functionName="delete_character_function")
	Output deleteCharacter(DeleteCharacterInput input);
}
