package com.gsciolti.kata.marsrover.functional

sealed class Either<out L, out R> {

    inline fun <T> map(f: (R) -> T): Either<L, T> =
        flatMap { f(it).right() }

    inline fun <M, T> bimap(leftF: (L) -> M, rightF: (R) -> T): Either<M, T> =
        fold({ leftF(it).left() }, { rightF(it).right() })

    inline fun <T> mapLeft(f: (L) -> T): Either<T, R> =
        fold({ Left(f(it)) }, { Right(it) })

    inline fun tap(block: (R) -> Unit): Either<L, R> =
        map {
            block(it)
            it
        }

    inline fun <T> fold(leftF: (L) -> T, rightF: (R) -> T): T =
        when (this) {
            is Left -> leftF(value)
            is Right -> rightF(value)
        }

    companion object {
        inline fun <R> catch(f: () -> R): Either<Throwable, R> =
            try {
                Right(f())
            } catch (t: Throwable) {
                Left(t)
            }
    }
}

inline fun <L, R, T> Either<L, R>.flatMap(f: (R) -> Either<L, T>): Either<L, T> =
    fold({ Left(it) }, { f(it) })

fun <L, R> Either<L, Either<L, R>>.flatten(): Either<L, R> =
    flatMap { it }

inline fun <L, R> Either<L, R>.foldRight(leftF: (L) -> R): R =
    fold(leftF) { it }

inline fun <L, R> Either<L, R>.getOrElse(leftF: (L) -> R): R =
    fold({ leftF(it) }, { it })

fun <R> Either<*, R>.orNull(): R? =
    getOrElse { null }

data class Left<out L>(val value: L) : Either<L, Nothing>()
data class Right<out R>(val value: R) : Either<Nothing, R>()

fun <T> T.left() = Left(this)
fun <T> T.right() = Right(this)

fun <L, R> R?.orEither(mapNull: () -> L): Either<L, R> =
    this?.right() ?: mapNull().left()
