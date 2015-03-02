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
package mage.sets.avacynrestored;

import mage.constants.CardType;
import mage.constants.Rarity;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.common.continuous.BoostSourceEffect;
import mage.abilities.effects.common.continuous.GainAbilityPairedEffect;
import mage.abilities.keyword.SoulbondAbility;
import mage.cards.CardImpl;
import mage.constants.Duration;
import mage.constants.Zone;

import java.util.UUID;

/**
 * @author noxx
 */
public class Stonewright extends CardImpl {

    private static final String ruleText = "As long as {this} is paired with another creature, each of those creatures has \"{R}: This creature gets +1/+0 until end of turn.\"";

    public Stonewright(UUID ownerId) {
        super(ownerId, 157, "Stonewright", Rarity.UNCOMMON, new CardType[]{CardType.CREATURE}, "{R}");
        this.expansionSetCode = "AVR";
        this.subtype.add("Human");
        this.subtype.add("Shaman");

        this.color.setRed(true);
        this.power = new MageInt(1);
        this.toughness = new MageInt(1);

        // Soulbond
        this.addAbility(SoulbondAbility.getInstance());

        // As long as Stonewright is paired with another creature, each of those creatures has "{R}: This creature gets +1/+0 until end of turn."
        Ability ability = new SimpleActivatedAbility(Zone.BATTLEFIELD, new BoostSourceEffect(1, 0, Duration.EndOfTurn), new ManaCostsImpl("{R}"));
        this.addAbility(new SimpleStaticAbility(Zone.BATTLEFIELD, new GainAbilityPairedEffect(ability, ruleText)));
    }

    public Stonewright(final Stonewright card) {
        super(card);
    }

    @Override
    public Stonewright copy() {
        return new Stonewright(this);
    }
}
