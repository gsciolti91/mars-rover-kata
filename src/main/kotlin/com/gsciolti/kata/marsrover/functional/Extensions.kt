package com.gsciolti.kata.marsrover.functional

fun <T, L, R> Iterable<T>.update(initialState: R, updateState: (R, T) -> Either<L, R>): Either<L, R> =
    fold(initialState.right() as Either<L, R>) { currentState, nextInput ->
        currentState.flatMap { state ->
            updateState(state, nextInput)
        }
    }

infix fun <A, B> A.and(b: B) = this to b