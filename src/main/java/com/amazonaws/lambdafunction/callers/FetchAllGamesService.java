package com.amazonaws.lambdafunction.callers;

import com.amazonaws.services.lambda.invoke.LambdaFunction;

public interface FetchAllGamesService {
	@LambdaFunction(functionName="fetch_all_games_function")
	Output fetchAllGames(FetchAllGamesInput input);
}
