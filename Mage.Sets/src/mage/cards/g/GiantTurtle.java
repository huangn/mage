/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.cards.g;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import mage.MageInt;
import mage.MageObjectReference;
import mage.abilities.Ability;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.effects.RestrictionEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.SubType;
import mage.constants.WatcherScope;
import mage.constants.Zone;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.permanent.Permanent;
import mage.watchers.Watcher;

/**
 *
 * @author TheElk801
 */
public class GiantTurtle extends CardImpl {

    public GiantTurtle(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{1}{G}{G}");

        this.subtype.add(SubType.TURTLE);
        this.power = new MageInt(2);
        this.toughness = new MageInt(4);

        // Giant Turtle can't attack if it attacked during your last turn.
        this.addAbility(new SimpleStaticAbility(Zone.BATTLEFIELD, new GiantTurtleCantAttackEffect()), new GiantTurtleWatcher());
    }

    public GiantTurtle(final GiantTurtle card) {
        super(card);
    }

    @Override
    public GiantTurtle copy() {
        return new GiantTurtle(this);
    }
}

class GiantTurtleCantAttackEffect extends RestrictionEffect {

    public GiantTurtleCantAttackEffect() {
        super(Duration.WhileOnBattlefield);
        staticText = "{this} can't attack if it attacked during your last turn";
    }

    public GiantTurtleCantAttackEffect(final GiantTurtleCantAttackEffect effect) {
        super(effect);
    }

    @Override
    public boolean applies(Permanent permanent, Ability source, Game game) {
        return permanent.getId().equals(source.getSourceId());
    }

    @Override
    public boolean canAttack(Permanent attacker, UUID defenderId, Ability source, Game game) {
        GiantTurtleWatcher watcher = (GiantTurtleWatcher) game.getState().getWatchers().get(GiantTurtleWatcher.class.getSimpleName());
        for (MageObjectReference mor : watcher.getAttackedLastTurnCreatures()) {
            if (attacker.equals(mor.getPermanent(game)) && attacker.getZoneChangeCounter(game) == mor.getZoneChangeCounter()) {
                return false;
            }
        }
        return false;
    }

    @Override
    public GiantTurtleCantAttackEffect copy() {
        return new GiantTurtleCantAttackEffect(this);
    }

}

class GiantTurtleWatcher extends Watcher {

    public final Set<MageObjectReference> attackedLastTurnCreatures = new HashSet<>();

    public GiantTurtleWatcher() {
        super(GiantTurtleWatcher.class.getSimpleName(), WatcherScope.GAME);
    }

    public GiantTurtleWatcher(final GiantTurtleWatcher watcher) {
        super(watcher);
        this.attackedLastTurnCreatures.addAll(watcher.attackedLastTurnCreatures);
    }

    @Override
    public void watch(GameEvent event, Game game) {
        if (event.getType() == GameEvent.EventType.ATTACKER_DECLARED) {
            this.attackedLastTurnCreatures.add(new MageObjectReference(event.getSourceId(), game));
        }
        if (event.getType() == GameEvent.EventType.BEGINNING_PHASE_PRE) {
            this.attackedLastTurnCreatures.clear();
        }
    }

    public Set<MageObjectReference> getAttackedLastTurnCreatures() {
        return this.attackedLastTurnCreatures;
    }

    @Override
    public GiantTurtleWatcher copy() {
        return new GiantTurtleWatcher(this);
    }
}
