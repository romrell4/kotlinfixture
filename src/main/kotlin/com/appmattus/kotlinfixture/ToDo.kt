package com.appmattus.kotlinfixture

@Suppress("unused")
object ToDo {
    // TODO Array<Object> both Kotlin and Java?

    // TODO Factory method construction - find static functions that return instance of the class?

    // TODO Rename ChainResolver to CompositeResolver?

    // TODO Iterables - Iterable.isAssignbleFrom(type) & is interface, then return mutable list, with rand no elements

    // TODO Maps - Map.isAssignbleFrom(type) & is interface, then return mutable map, with rand no elements

    // TODO Queue - Queue.isAssignableFrom(tyoe) & is interface, then return ArrayDeque

    // TODO Seeded requests - basically a way to ensure consistent execution

    // TODO Set - Set.isAssignableFrom(type) & is interface, the return HashSet

    // TODO Customisations:
    // TODO - instance<T : Any?>(factory: Boolean, () -> T)
    // TODO - fixed subtype - useSubType<Iterable, LinkedList>()
    // TODO - propertyOf<Order>("fieldName", 123)
    // TODO - repeatCount = 5

    // TODO handle & detect circular dependencies, A has property B, B has property A etc. See circularDependencyBehaviour

    // TODO handle unresolvable, throw exception or return null. See noResolutionBehaviour
}
