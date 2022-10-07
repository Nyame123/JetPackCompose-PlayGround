package com.bismark.feature.foryou
/**
 * A sealed hierarchy for the user's current followed interests state.
 */
sealed interface FollowedInterestsState {

    /**
     * The current state is unknown (hasn't loaded yet)
     */
    object Unknown : FollowedInterestsState

    /**
     * The user hasn't followed any interests yet.
     */
    object None : FollowedInterestsState

    /**
     * The user has followed the given (non-empty) set of [topicIds] or [authorIds].
     */
    data class FollowedInterests(
        val topicIds: Set<String>,
        val authorIds: Set<String>
    ) : FollowedInterestsState
}
