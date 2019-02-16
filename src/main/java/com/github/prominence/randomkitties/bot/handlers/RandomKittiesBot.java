/*
 * Copyright (c) 2019 Alexey Zinchenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.github.prominence.randomkitties.bot.handlers;

import com.github.prominence.randomkitties.bot.model.Kitty;
import com.github.prominence.randomkitties.bot.util.CatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class RandomKittiesBot extends TelegramLongPollingBot {

    private static final Logger log = LogManager.getLogger(RandomKittiesBot.class);

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            final Message message = update.getMessage();
            if (message.hasText()) {

                final String requestText = message.getText();
                final String sender = message.getFrom().toString();

                final Long chatId = message.getChatId();

                logCommunication(sender, requestText);

                try {
                    switch (requestText) {
                        case "/kitty":
                        case "/kitty@RandomKittiesBot":
                            sendPhotosResponse(chatId, 1);
                            break;
                        case "/more_kitties":
                        case "/more_kitties@RandomKittiesBot":
                            sendPhotosResponse(chatId, 3);
                            break;
                        case "/moooooore_kitties":
                        case "/moooooore_kitties@RandomKittiesBot":
                            sendPhotosResponse(chatId, 5);
                            break;
                        case "/help":
                        case "/help@RandomKittiesBot":
                        default:
                            sendHelp(chatId);
                            break;
                    }
                } catch (TelegramApiException ex) {
                    ex.printStackTrace();
                    log.error(ex);
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "RandomKittiesBot";
    }

    @Override
    public String getBotToken() {
        return "<YOUR BOT API KEY FROM BotFather>";
    }

    private void sendHelp(Long chatId) throws TelegramApiException {
        execute(new SendMessage(chatId, "Please, use on of the next commands: /kitty, /more_kitties, /moooooore_kitties"));
    }

    private void sendPhotosResponse(Long chatId, int amount) throws TelegramApiException {
        SendMediaGroup mediaGroup = new SendMediaGroup().setChatId(chatId);
        List<InputMedia> mediaList = new ArrayList<>(amount);

        final List<Kitty> kittiesList = CatUtils.getRandomCatsList(amount);

        kittiesList.forEach(kitty -> mediaList.add(new InputMediaPhoto(kitty.getUrl(), null)));

        mediaGroup.setMedia(mediaList);

        execute(mediaGroup);
    }

    private void logCommunication(String sender, String requestText) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n=========== REQUEST ===========\n");
        builder.append("Sender : ");
        builder.append(sender);
        builder.append('\n');
        builder.append("Request text: ");
        builder.append(requestText);
        builder.append('\n');

        log.debug(builder.toString());
    }

}
