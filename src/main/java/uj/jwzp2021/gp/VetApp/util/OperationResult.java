package uj.jwzp2021.gp.VetApp.util;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public final class OperationResult<F, S> {

  private final Optional<F> fail;
  private final Optional<S> success;

  private OperationResult(Optional<F> f, Optional<S> s) {
    fail = f;
    success = s;
  }

  public static <F, S> OperationResult<F, S> fail(F value) {
    return new OperationResult<>(Optional.of(value), Optional.empty());
  }

  public static <F, S> OperationResult<F, S> success(S value) {
    return new OperationResult<>(Optional.empty(), Optional.of(value));
  }

  public <T> T map(
      Function<? super F, ? extends T> failFunc, Function<? super S, ? extends T> successFunc) {
    return fail.<T>map(failFunc).orElseGet(() -> success.map(successFunc).get());
  }

  public <T> OperationResult<T, S> mapFail(Function<? super F, ? extends T> failFunc) {
    return new OperationResult<>(fail.map(failFunc), success);
  }

  public <T> OperationResult<F, T> mapSuccess(Function<? super S, ? extends T> successFunc) {
    return new OperationResult<>(fail, success.map(successFunc));
  }

  public void apply(Consumer<? super F> failFunc, Consumer<? super S> successFunc) {
    fail.ifPresent(failFunc);
    success.ifPresent(successFunc);
  }
}
