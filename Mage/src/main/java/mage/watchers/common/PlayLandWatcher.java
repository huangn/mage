package mage.watchers.common;

import mage.MageObjectReference;
import mage.constants.WatcherScope;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.permanent.Permanent;
import mage.watchers.Watcher;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author jeffwadsworth
 */
public class PlayLandWatcher extends Watcher {

    private final Set<UUID> playerPlayedLand = new HashSet<>(); // player that played land
    private final Set<MageObjectReference> landPlayed = new HashSet<>(); // land played

    public PlayLandWatcher() {
        super(WatcherScope.GAME);
    }

    @Override
    public void watch(GameEvent event, Game game) {
        if (event.getType() == GameEvent.EventType.LAND_PLAYED) {
            Permanent permanent = game.getPermanentOrLKIBattlefield(event.getTargetId());
            if (permanent != null && permanent.isLand(game)) {
                MageObjectReference mor = new MageObjectReference(permanent, game);
                landPlayed.add(mor);
                playerPlayedLand.add(event.getPlayerId());
            }
        }
    }

    @Override
    public void reset() {
        super.reset();
        playerPlayedLand.clear();
        landPlayed.clear();
    }

    public boolean landPlayed(UUID playerId) {
        return playerPlayedLand.contains(playerId);
    }

    public boolean wasLandPlayed(Permanent land, Game game) {
        return landPlayed.contains(new MageObjectReference(land, game));
    }
}
