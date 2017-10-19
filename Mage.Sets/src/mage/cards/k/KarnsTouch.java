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
package mage.cards.k;

import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.effects.ContinuousEffectImpl;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.Layer;
import mage.constants.Outcome;
import mage.constants.SubLayer;
import mage.filter.common.FilterArtifactPermanent;
import mage.filter.predicate.Predicates;
import mage.filter.predicate.mageobject.CardTypePredicate;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.target.TargetPermanent;

/**
 *
 * @author TheElk801
 */
public class KarnsTouch extends CardImpl {

    private static final FilterArtifactPermanent filter = new FilterArtifactPermanent("noncreature artifact");

    static {
        filter.add(Predicates.not(new CardTypePredicate(CardType.CREATURE)));
    }

    public KarnsTouch(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.INSTANT}, "{U}{U}");

        // Target noncreature artifact becomes an artifact creature with power and toughness each equal to its converted mana cost until end of turn.
        this.getSpellAbility().addEffect(new KarnsTouchEffect());
        this.getSpellAbility().addTarget(new TargetPermanent(filter));
    }

    public KarnsTouch(final KarnsTouch card) {
        super(card);
    }

    @Override
    public KarnsTouch copy() {
        return new KarnsTouch(this);
    }
}

class KarnsTouchEffect extends ContinuousEffectImpl {

    public KarnsTouchEffect() {
        super(Duration.EndOfTurn, Outcome.BecomeCreature);
        staticText = "Target noncreature artifact becomes an artifact creature with power and toughness each equal to its converted mana cost until end of turn";
    }

    public KarnsTouchEffect(final KarnsTouchEffect effect) {
        super(effect);
    }

    @Override
    public KarnsTouchEffect copy() {
        return new KarnsTouchEffect(this);
    }

    @Override
    public boolean apply(Layer layer, SubLayer sublayer, Ability source, Game game) {
        Permanent artifact = game.getPermanent(this.getTargetPointer().getFirst(game, source));
        if (artifact == null) {
            return false;
        }
        switch (layer) {
            case TypeChangingEffects_4:
                if (sublayer == SubLayer.NA) {
                    if (!artifact.isCreature()) {
                        artifact.addCardType(CardType.CREATURE);
                    }
                }
                break;

            case PTChangingEffects_7:
                if (sublayer == SubLayer.SetPT_7b) {
                    int cmc = artifact.getConvertedManaCost();
                    artifact.getPower().setValue(cmc);
                    artifact.getToughness().setValue(cmc);
                }
        }
        return true;
    }

    @Override
    public boolean apply(Game game, Ability source) {
        return false;
    }

    @Override
    public boolean hasLayer(Layer layer) {
        return layer == Layer.PTChangingEffects_7 || layer == Layer.TypeChangingEffects_4;
    }
}
