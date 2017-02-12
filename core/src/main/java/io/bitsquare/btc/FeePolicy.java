/*
 * This file is part of Bitsquare.
 *
 * Bitsquare is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bitsquare is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bitsquare. If not, see <http://www.gnu.org/licenses/>.
 */

package io.bitsquare.btc;

import org.bitcoinj.core.Coin;

public class FeePolicy {

    // With block getting filled up the needed fee to get fast into a black has become more expensive and less predictable.
    // To see current fees check out:
    // https://tradeblock.com/blockchain
    // https://jochen-hoenicke.de/queue/24h.html
    // https://bitcoinfees.21.co/
    // http://p2sh.info/dashboard/db/fee-estimation
    // https://bitcoinfees.github.io/#1d
    // https://estimatefee.appspot.com/
    // Average values are 10-100 satoshis/byte in january 2016
    // Average values are 60-140 satoshis/byte in february 2017
    // 
    // Our trade transactions have a fixed set of inputs and outputs making the size very predictable 
    // (as long the user does not do multiple funding transactions)
    // 
    // trade fee tx: 226 bytes          // 221 satoshi/byte
    // deposit tx: 336 bytes            // 148 satoshi/byte
    // payout tx: 371 bytes             // 134 satoshi/byte
    // disputed payout tx: 408 bytes    // 122 satoshi/byte

    // We set a fixed fee to make the needed amounts in the trade predictable.
    // We use 0.0005 BTC (0.5 EUR @ 1000 EUR/BTC) which is for our tx sizes about 120-220 satoshi/byte
    // We cannot make that user defined as it need to be the same for both users, so we can only change that in 
    // software updates 
    // TODO before Beta we should get a good future proof guess as a change causes incompatible versions

    // For non trade transactions (withdrawal) we use the default fee calculation 
    // To avoid issues with not getting into full blocks, we increase the fee/kb to 30 satoshi/byte
    // The user can change that in the preferences 
    // The BitcoinJ fee calculation use kb so a tx size  < 1kb will still pay the fee for a kb tx.
    // Our payout tx has about 370 bytes so we get a fee/kb value of about 90 satoshi/byte making it high priority
    // Other payout transactions (E.g. arbitrators many collected transactions) will go with 30 satoshi/byte if > 1kb
    private static Coin NON_TRADE_FEE_PER_KB = Coin.valueOf(40_000); // 0.0004 BTC about 0.16 EUR @ 400 EUR/BTC 

    public static void setNonTradeFeePerKb(Coin nonTradeFeePerKb) {
        NON_TRADE_FEE_PER_KB = nonTradeFeePerKb;
    }

    public static Coin getNonTradeFeePerKb() {
        return NON_TRADE_FEE_PER_KB;
    }

}
