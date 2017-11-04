package com.amazonaws.lambdafunction.callers;

import com.amazonaws.services.lambda.invoke.LambdaFunction;

/*
 * Interface to define interaction with the
 * lambda function create_game_function
 */
public interface CreateGameService {
	@LambdaFunction(functionName="create_game_function")
	Output createGame(CreateGameInput input);
}