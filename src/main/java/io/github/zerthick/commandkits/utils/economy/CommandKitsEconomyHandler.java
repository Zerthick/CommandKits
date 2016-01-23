/*
 * Copyright (C) 2016  Zerthick
 *
 * This file is part of CommandKits.
 *
 * CommandKits is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * CommandKits is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CommandKits.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.zerthick.commandkits.utils.economy;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import java.math.BigDecimal;
import java.util.Optional;

public class CommandKitsEconomyHandler {

    private static CommandKitsEconomyHandler instance;
    private EconomyService economyService;

    protected CommandKitsEconomyHandler() {
        //Singleton Design Pattern
    }

    public static CommandKitsEconomyHandler getInstance() {
        if (instance == null) {
            instance = new CommandKitsEconomyHandler();
        }
        return instance;
    }

    public void setUp(EconomyService economyService) {
        this.economyService = economyService;
    }

    public BigDecimal getPlayerBalance(Player player) {
        if (economyService != null) {
            Optional<UniqueAccount> uOpt = economyService.getAccount(player.getUniqueId());
            if (uOpt.isPresent()) {
                UniqueAccount acc = uOpt.get();
                return acc.getBalance(economyService.getDefaultCurrency());
            }
        }
        return BigDecimal.valueOf(0);
    }
}
