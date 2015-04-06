package jp.t2v.xanadu.ops;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is expected that is is used with @ExtensionMethod.
 */
@UtilityClass
public class ListOps {

    @SuppressWarnings("unchecked")
    public <A, B> List<B> flatMap(final List<A> self, final Function<? super A, ? extends Iterable<? extends B>> f) {
        return StreamOps.flatMapI(self.stream(), f).collect(Collectors.toList());
    }

    public <A, B> List<B> flatMapO(final List<A> self, final Function<? super A, Optional<? extends B>> f) {
        return StreamOps.flatMapO(self.stream(), f).collect(Collectors.toList());
    }

    public <A, B> List<B> map(final List<A> self, final Function<? super A, ? extends B> f) {
        return self.stream().map(f).collect(Collectors.toList());
    }

    public <A, B, C> List<C> map2(final List<A> self, final Iterable<B> other, final BiFunction<? super A, ? super B, ? extends C> f) {
        return StreamOps.map2(self.stream(), IterableOps.stream(other), f).collect(Collectors.toList());
    }

    public <A> boolean allMatch(final List<A> self, final Predicate<? super A> p) {
        return self.stream().allMatch(p);
    }

    public <A> boolean anyMatch(final List<A> self, final Predicate<? super A> p) {
        return self.stream().anyMatch(p);
    }

    public <A> Optional<A> maxBy(final List<A> self, final Comparator<? super A> c) {
        return self.stream().max(c);
    }

    public <A> Optional<A> minBy(final List<A> self, final Comparator<? super A> c) {
        return self.stream().min(c);
    }

    public <A extends Comparable<A>> Optional<A> max(final List<A> self) {
        return maxBy(self, Comparator.naturalOrder());
    }

    public <A extends Comparable<A>> Optional<A> min(final List<A> self) {
        return minBy(self, Comparator.naturalOrder());
    }

    public <A, B> B foldLeft(final List<A> self, B init, BiFunction<? super B, ? super A, ? extends B> f) {
        B sentinel = init;
        for (final A a : self) {
            sentinel = f.apply(sentinel, a);
        }
        return sentinel;
    }

    public <A> Optional<List<A>> sequenceO(final List<Optional<? extends A>> self) {
        return traverseO(self, Function.identity());
    }

    public <A, B> Optional<List<B>> traverseO(final List<A> self, Function<? super A, Optional<? extends B>> f) {
        return foldLeft(self, Optional.of(new ArrayList<B>()), (acc, e) -> OptionalOps.map2(acc, f.apply(e), (xs, x) -> {xs.add(x); return xs;}));
    }

}
