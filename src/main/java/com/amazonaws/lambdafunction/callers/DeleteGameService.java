package com.amazonaws.lambdafunction.callers;

import com.amazonaws.services.lambda.invoke.LambdaFunction;

public interface DeleteGameService {
	@LambdaFunction(functionName="delete_game_function")
	Output deleteGame(DeleteGameInput input);
}
