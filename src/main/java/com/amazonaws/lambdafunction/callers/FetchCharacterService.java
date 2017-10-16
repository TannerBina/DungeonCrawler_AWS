package com.amazonaws.lambdafunction.callers;

import com.amazonaws.services.lambda.invoke.LambdaFunction;

/*
 * Interacts with fetch_character_function
 */
public interface FetchCharacterService {
	@LambdaFunction(functionName="fetch_character_function")
	Output fetchCharacter(FetchCharacterInput input);
}
