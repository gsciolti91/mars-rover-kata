package com.gsciolti.kata.marsrover.functional

infix fun <A, B, C> ((A) -> B).andThen(g: (B) -> C): (A) -> C =
    { a -> g(this(a)) }

infix fun <A, B, C, D> ((A, B) -> C).andThen(g: (C) -> D): (A, B) -> D =
    { a, b -> g(this(a, b)) }

fun <T, L, R> Iterable<T>.update(initialState: R, updateState: (R, T) -> Either<L, R>): Either<L, R> =
    fold(initialState.right() as Either<L, R>) { currentState, nextInput ->
        currentState.flatMap { state ->
            updateState(state, nextInput)
        }
    }

fun <L, R> R.check(checks: Collection<(R) -> Either<L, R>>): Either<L, R> =
    checks.fold(this.right() as Either<L, R>) { result, nextCheck -> result.flatMap(nextCheck) }

infix fun <A, B> A.and(b: B) = this to b
