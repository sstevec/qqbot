package com.botdemo.demo1.service;


import com.botdemo.demo1.dao.Message;
import com.botdemo.demo1.dao.PersonalInfo;
import com.botdemo.demo1.dao.PickUpResult;
import com.botdemo.demo1.dao.Sender;
import com.botdemo.demo1.mapper.PcrMapper;
import com.botdemo.demo1.mapper.SentenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Service
public class EventProcessorImp implements EventProcessor {
    @Autowired
    MessageAssemble messageAssemble;

    @Autowired
    LocalFileSystem localFileSystem;

    @Autowired
    MessageSender messageSender;

    @Autowired
    PcrMapper pcrMapper;

    @Autowired
    SentenceMapper sentenceMapper;

    @Autowired
    InfoQuery infoQuery;


    @Override
    public void groupEventProcessor(String groupId, String message, Sender sender, String selfId) {
        // test 791372943, pcr 597496324
        if (groupId.equals("597496324")) {
            // pcr 群
            pcrEventProcessor(groupId, message, sender, selfId);
        }
    }

    public void pcrEventProcessor(String groupId, String message, Sender sender, String selfId) {
        String children = "[CQ:at,qq=" + selfId + "]";
        int index = message.indexOf(children);
        if (index == 0) {
            // find the @ sign

            // process the message
            if (message.length() != children.length()) {
                // not just the @sign
                message = message.substring(children.length() + 1);
                System.out.println(message);
            }

            Random random = new Random();
            if (message.equals("来一井")) {
                int res = random.nextInt(2);
                String result;
                if (res == 0) {
                    result = pickThreeHundred(1);
                } else {
                    messageSender.sendMessageToGroup(messageAssemble.assembleGroupMessage(groupId, "对方不想模拟抽井"));
                    result = autoResponse(groupId, message, sender, selfId);
                }

                messageSender.sendMessageToGroup(messageAssemble.assembleGroupMessage(groupId, result));
            } else if (message.equals("来张图")) {
                int res = random.nextInt(2);
                String result;

                if (res == 0) {
                    result = localFileSystem.generateCQForLocalFile(0);
                } else {
                    messageSender.sendMessageToGroup(messageAssemble.assembleGroupMessage(groupId, "ghs系统正在冷却~"));
                    result = autoResponse(groupId, message, sender, selfId);
                }

                messageSender.sendMessageToGroup(messageAssemble.assembleGroupMessage(groupId, result));
            } else if (message.equals("来点语音")) {
                int res = random.nextInt(2);
                String result;

                if (res == 0) {
                    result = localFileSystem.generateCQForLocalFile(1);
                } else {
                    messageSender.sendMessageToGroup(messageAssemble.assembleGroupMessage(groupId, "等一下啦，lsp"));
                    result = autoResponse(groupId, message, sender, selfId);
                }

                messageSender.sendMessageToGroup(messageAssemble.assembleGroupMessage(groupId, result));
            } else {
                String result = autoResponse(groupId, message, sender, selfId);
                messageSender.sendMessageToGroup(messageAssemble.assembleGroupMessage(groupId, result));
            }

        } else {
            // no @, building


//                try {
//                    pcrGroupEvent(groupId, message, sender, selfId);
//                } catch (Exception e) {
//                    messageSender.sendMessageToGroup(messageAssemble.assembleGroupMessage(groupId, "指令错误, 无法执行"));
//                }

        }
    }

    @Override
    public void pcrBattleEvent(String groupId, String message, Sender sender, String selfId) {
        Message m;
        if (message.equals("申请出刀")) {
            HashMap<String, Object> info = pcrMapper.getCurrentInfo();
            if (info.get("combine_code").toString().equals("1")) {
                // 等待合刀
                m = messageAssemble.assembleGroupMessage(groupId, info.get("card").toString() + "正在等待合刀队友");
                messageSender.sendMessageToGroup(m);
                return;
            } else if (info.get("combine_code").toString().equals("2")) {
                // 正在合刀
                m = messageAssemble.assembleGroupMessage(groupId, info.get("card").toString() + "和" + info.get("card_2").toString() + "正在合刀, 请等待");
                messageSender.sendMessageToGroup(m);
                return;
            }
            if (!info.get("user_id").toString().equals("no_one")) {
                // 有人在出刀
                m = messageAssemble.assembleGroupMessage(groupId, info.get("card").toString() + "正在出刀");
                messageSender.sendMessageToGroup(m);
                return;
            }
            HashMap<String, Object> memberInfo = pcrMapper.getMemberData(sender.getUser_id());
            int battleRemain = (int) memberInfo.get("battle_remain");
            String memberName = memberInfo.get("card").toString();
            if (battleRemain > 0) {
                battleRemain--;
                m = messageAssemble.assembleGroupMessage(groupId, memberName + "剩余" + battleRemain + "刀");
                pcrMapper.setMemberData(sender.getUser_id(), battleRemain);
                pcrMapper.setCurrentMember(sender.getUser_id(), sender.getCard(), 0);
            } else {
                m = messageAssemble.assembleGroupMessage(groupId, memberName + "别捣乱");
            }
            messageSender.sendMessageToGroup(m);
        } else if (message.equals("申请合刀")) {
            HashMap<String, Object> info = pcrMapper.getCurrentInfo();
            if (info.get("combine_code").toString().equals("1")) {
                // 等待合刀, 加入
                if (info.get("user_id").toString().equals(sender.getUser_id())) {
                    m = messageAssemble.assembleGroupMessage(groupId, sender.getCard() + "你无法与自己合刀");
                    messageSender.sendMessageToGroup(m);
                    return;
                }
                HashMap<String, Object> memberInfo = pcrMapper.getMemberData(sender.getUser_id());
                int battleRemain = (int) memberInfo.get("battle_remain");
                if (battleRemain > 0) {
                    pcrMapper.setCombineMember(sender.getUser_id(), sender.getCard(), 2);
                    m = messageAssemble.assembleGroupMessage(groupId, sender.getCard() + "已加入合刀, 合刀开始, 提示: 合刀结束后只有结算的人需要发送\"完成合刀\"");
                } else {
                    m = messageAssemble.assembleGroupMessage(groupId, sender.getCard() + "别捣乱");
                }
                messageSender.sendMessageToGroup(m);
                return;
            } else if (info.get("combine_code").toString().equals("2")) {
                // 正在合刀
                m = messageAssemble.assembleGroupMessage(groupId, info.get("card").toString() + "和" + info.get("card_2").toString() + "正在合刀, 请等待");
                messageSender.sendMessageToGroup(m);
                return;
            } else {
                if (!info.get("user_id").toString().equals("no_one")) {
                    // 有人在出刀
                    m = messageAssemble.assembleGroupMessage(groupId, info.get("card").toString() + "正在出刀");
                    messageSender.sendMessageToGroup(m);
                    return;
                } else {
                    // open
                    HashMap<String, Object> memberInfo = pcrMapper.getMemberData(sender.getUser_id());
                    int battleRemain = (int) memberInfo.get("battle_remain");
                    if (battleRemain > 0) {
                        m = messageAssemble.assembleGroupMessage(groupId, sender.getCard() + "正在等待队友合刀");
                        pcrMapper.setCurrentMember(sender.getUser_id(), sender.getCard(), 1);
                    } else {
                        m = messageAssemble.assembleGroupMessage(groupId, sender.getCard() + "别捣乱");
                    }
                }
            }
            messageSender.sendMessageToGroup(m);
        } else if (message.equals("完成合刀")) {
            // 合刀完成, boss必死, 发合刀完成的人扣除次数
            HashMap<String, Object> memberInfo = pcrMapper.getMemberData(sender.getUser_id());
            int battleRemain = (int) memberInfo.get("battle_remain");
            battleRemain--;
            pcrMapper.setMemberData(sender.getUser_id(), battleRemain);
            m = messageAssemble.assembleGroupMessage(groupId, sender.getCard() + "剩余" + battleRemain + "刀");
            messageSender.sendMessageToGroup(m);

            // load new boss
            HashMap<String, Object> info = pcrMapper.getCurrentInfo();
            int currentBossNumber = (int) info.get("boss_number");
            // boss die, load next
            pcrMapper.killBoss(currentBossNumber);
            int totalBossNumber = pcrMapper.getBossNumber();
            if (currentBossNumber < totalBossNumber) {
                // can load next
                currentBossNumber++;
                HashMap<String, Object> bossInfo = pcrMapper.getNextBoss(currentBossNumber);
                pcrMapper.loadNewBoss(currentBossNumber, bossInfo.get("boss_current_health").toString(), bossInfo.get("boss_max_health").toString());
                m = messageAssemble.assembleGroupMessage(groupId, "已加载" + currentBossNumber + "号Boss， 当前血量" + bossInfo.get("boss_current_health").toString());
            } else {
                m = messageAssemble.assembleGroupMessage(groupId, "当前录入boss全部讨伐完毕");
            }
            messageSender.sendMessageToGroup(m);
        } else if (message.equals("更新群成员信息") && sender.getUser_id().equals("303983558")) {
            List<PersonalInfo> allMemberInfo = infoQuery.getAllGroupMember(groupId);
            ArrayList<HashMap<String, Object>> dataBaseInfo = pcrMapper.getAllMemberData();
            for (PersonalInfo personalInfo : allMemberInfo) {
                for (int j = 0; j < dataBaseInfo.size(); j++) {
                    if (personalInfo.getUser_id().equals(dataBaseInfo.get(j).get("user_id").toString())) {
                        // check card
                        if (personalInfo.getCard().equals(dataBaseInfo.get(j).get("card").toString())) {
                            break;
                        } else {
                            pcrMapper.updateMemberName(personalInfo.getUser_id(), personalInfo.getCard());
                        }
                    }
                    if (j == dataBaseInfo.size() - 1) {
                        // add new member
                        pcrMapper.addOneMember(personalInfo.getUser_id(), personalInfo.getCard());
                    }
                }
            }
            m = messageAssemble.assembleGroupMessage(groupId, "更新完毕");
            messageSender.sendMessageToGroup(m);
//            && sender.getUser_id().equals("1515055083")
        } else if (message.equals("录入全部成员信息") && sender.getUser_id().equals("303983558")) {
            List<PersonalInfo> allMemberInfo = infoQuery.getAllGroupMember(groupId);
            String formName = "group_chart";
            String[] columnName = {"user_id", "card", "battle_remain"};
            String[][] data = new String[allMemberInfo.size()][3];
            for (int i = 0; i < allMemberInfo.size(); i++) {
                data[i][0] = allMemberInfo.get(i).getUser_id();
                data[i][1] = allMemberInfo.get(i).getCard();
                data[i][2] = "3";
            }
            pcrMapper.autoWriteIn(formName, columnName, data);
            m = messageAssemble.assembleGroupMessage(groupId, "录入完毕");
            messageSender.sendMessageToGroup(m);
        } else if (message.equals("重置所有刀数")) {
            pcrMapper.resetAllBattleNumber();
            m = messageAssemble.assembleGroupMessage(groupId, "刀数已重置");
            messageSender.sendMessageToGroup(m);
        } else if (message.equals("查看出刀状态")) {
            ArrayList<HashMap<String, Object>> dataBaseInfo = pcrMapper.getAllMemberData();
            StringBuilder notice = new StringBuilder();
            for (HashMap<String, Object> stringObjectHashMap : dataBaseInfo) {
                int battleRemain = (int) stringObjectHashMap.get("battle_remain");
                String card = stringObjectHashMap.get("card").toString();
                String userId = stringObjectHashMap.get("user_id").toString();
                if (battleRemain > 0 && !userId.equals(selfId)) {
                    notice.append(card).append(" id:").append(userId).append(" 剩余").append(battleRemain).append("刀\n");
                }
            }
            if (notice.toString().equals("")) {
                notice.append("所有成员均已出刀");
            }
            m = messageAssemble.assembleGroupMessage(groupId, notice.toString());
            messageSender.sendMessageToGroup(m);
        } else if (message.equals("重置出刀状态")) {
            pcrMapper.clearCurrentMember();
            m = messageAssemble.assembleGroupMessage(groupId, "出刀状态已重置,现在无人出刀");
            messageSender.sendMessageToGroup(m);
        } else if (message.equals("挂树帮助")) {
            m = messageAssemble.assembleGroupMessage(groupId, "宝贝, 树上凉快吗, 你挂树了不找会长找我有什么用???");
            messageSender.sendMessageToGroup(m);
        } else if (message.equals("命令列表")) {
            String notice = "输入\"查看出刀状态\"获取所有成员出刀状态\n" +
                    "输入\"重置所有刀数\"重置所有成员出刀状态\n" +
                    "输入\"申请出刀\"记录出刀状态，和排刀机器人一致\n" +
                    "输入\"申请合刀\"开启合刀模式, 第一遍输入后会等待队友加入, 队友需要输入\"申请合刀\"来加入\n" +
                    "输入\"完成合刀\"结束合刀状态，仅需要结算的人发送, 白嫖的人不需要发\n" +
                    "输入\"完成 伤害数字\"结束出刀，示例\"完成 10000\"\n" +
                    "输入\"更改当前boss boss号码 boss当前血量 boss总血量(单位w)\"更改当前挑战的boss， 示例\"设置boss 5 121213 2000\"\n" +
                    "输入\"修正当前boss boss号码 boss当前血量 boss总血量(单位w)\"设置boss血量， 示例\"设置boss 4 121213 1200\"\n" +
                    "输入\"重置出刀状态\"清空当前出刀槽位\n" +
                    "输入\"修正我的刀数 修正后的数字\"设置发言成员的剩余刀数\n" +
                    "输入\"修正刀数 需要修正的成员的qq号 修正后的数字\"设置目标成员的剩余刀数\n" +
                    "输入\"挂树帮助\"获取挂树帮助\n";
            m = messageAssemble.assembleGroupMessage(groupId, notice);
            messageSender.sendMessageToGroup(m);
        } else {
            // 复合命令
            String[] orders = message.split(" ");
            if (orders.length > 0) {
                // have something
                if (orders[0].equals("添加boss")) {
                    int lastNumber = pcrMapper.getBossNumber();
                    for (int i = 1; i < orders.length; i++) {
                        lastNumber++;
                        int health = Integer.parseInt(orders[i]);
                        String actualHealth = "" + health + "0000";
                        pcrMapper.addBoss(lastNumber, actualHealth);
                        m = messageAssemble.assembleGroupMessage(groupId, "已加载第" + lastNumber + "个Boss");
                        messageSender.sendMessageToGroup(m);
                    }
                } else if (orders[0].equals("修正当前boss")) {
                    if (orders.length == 4) {
                        int lastNumber = Integer.parseInt(orders[1]);
                        String currentHealth = orders[2];
                        String maxHealth = orders[3];
                        maxHealth = maxHealth + "0000";
                        pcrMapper.setBossHealthGeneral(lastNumber, currentHealth, maxHealth);

                        HashMap<String, Object> info = pcrMapper.getCurrentInfo();
                        if (Integer.parseInt(info.get("boss_number").toString()) == lastNumber) {
                            pcrMapper.loadNewBoss(lastNumber, currentHealth, maxHealth);
                        }
                        m = messageAssemble.assembleGroupMessage(groupId, "boss 血量设置完成");
                    } else {
                        m = messageAssemble.assembleGroupMessage(groupId, "需要3个变量，boss号码, 当前血量, 总血量(w)");
                    }
                    messageSender.sendMessageToGroup(m);
                } else if (orders[0].equals("更改当前boss")) {
                    if (orders.length == 4) {
                        int lastNumber = Integer.parseInt(orders[1]);
                        String currentHealth = orders[2];
                        String maxHealth = orders[3];
                        maxHealth = maxHealth + "0000";

                        pcrMapper.loadNewBoss(lastNumber, currentHealth, maxHealth);

                        m = messageAssemble.assembleGroupMessage(groupId, "当前boss 已更改为" + lastNumber + "号, 当前血量" + currentHealth);
                    } else {
                        m = messageAssemble.assembleGroupMessage(groupId, "需要3个变量，boss号码, 当前血量, 总血量(w)");
                    }
                    messageSender.sendMessageToGroup(m);
                } else if (orders[0].equals("完成")) {
                    if (orders.length != 2) {
                        return;
                    }
                    String damage = orders[1];
                    HashMap<String, Object> info = pcrMapper.getCurrentInfo();
                    if (!info.get("user_id").toString().equals(sender.getUser_id())) {
                        return;
                    }
                    String currentHealth = info.get("boss_health").toString();
                    long newHealth = Long.parseLong(currentHealth) - Long.parseLong(damage);
                    int currentBossNumber = (int) info.get("boss_number");
                    if (newHealth <= 0) {
                        // boss die, load next
                        pcrMapper.killBoss(currentBossNumber);
                        int totalBossNumber = pcrMapper.getBossNumber();
                        if (currentBossNumber < totalBossNumber) {
                            // can load next
                            currentBossNumber++;
                            HashMap<String, Object> bossInfo = pcrMapper.getNextBoss(currentBossNumber);
                            pcrMapper.loadNewBoss(currentBossNumber, bossInfo.get("boss_current_health").toString(), bossInfo.get("boss_max_health").toString());
                            m = messageAssemble.assembleGroupMessage(groupId, "已加载" + currentBossNumber + "号Boss， 当前血量" + bossInfo.get("boss_current_health").toString());
                        } else {
                            m = messageAssemble.assembleGroupMessage(groupId, "当前录入boss全部讨伐完毕");
                        }
                    } else {
                        // reduce health
                        pcrMapper.setBossHealth(newHealth + "");
                        double totalHealth = Double.parseDouble(info.get("boss_max_health").toString());
                        double damagePercent = Integer.parseInt(damage) / totalHealth * 100;
                        double remainPercent = newHealth / totalHealth * 100;
                        m = messageAssemble.assembleGroupMessage(groupId, currentBossNumber + "号Boss剩余血量" + newHealth + ", 伤害比例" + damagePercent + "%, 剩余血量比例" + remainPercent + "%");
                    }
                    messageSender.sendMessageToGroup(m);
                } else if (orders[0].equals("修正我的刀数")) {
                    if (orders.length != 2) {
                        return;
                    }
                    int newNumber = Integer.parseInt(orders[1]);
                    if (newNumber >= 0 && newNumber <= 3) {
                        pcrMapper.setMemberData(sender.getUser_id(), newNumber);
                        m = messageAssemble.assembleGroupMessage(groupId, sender.getCard() + "的刀数已修正为" + newNumber);
                    } else {
                        m = messageAssemble.assembleGroupMessage(groupId, "刀数值不合理,无法修正");
                    }
                    messageSender.sendMessageToGroup(m);
                } else if (orders[0].equals("修正刀数")) {
                    if (orders.length != 3) {
                        return;
                    }
                    String userId = orders[1];
                    int newNumber = Integer.parseInt(orders[2]);
                    if (newNumber >= 0 && newNumber <= 3) {
                        pcrMapper.setMemberData(userId, newNumber);
                        m = messageAssemble.assembleGroupMessage(groupId, userId + " 的刀数已修正为" + newNumber);
                    } else {
                        m = messageAssemble.assembleGroupMessage(groupId, "刀数值不合理,无法修正");
                    }
                    messageSender.sendMessageToGroup(m);
                }
            }
        }
    }


    public String autoResponse(String groupId, String message, Sender sender, String selfId) {
        String content = "";

        int totalNumber = sentenceMapper.getTotalResponseNumber();
        Random random = new Random();
        int id = random.nextInt(totalNumber) + 1;
        String sentence = sentenceMapper.getResponseSentence(id);
        content = content + sentence;

        return content;
    }

    // type = 0 normal, type = 1 pick up
    public String pickThreeHundred(int type) {
        PickUpResult result = new PickUpResult();
        if (type == 0) {
            // normal
            for (int i = 0; i < 30; i++) {
                PickUpResult temp = pickTenNormal();
                result.appendResult(temp);
            }
        } else if (type == 1) {
            for (int i = 0; i < 30; i++) {
                PickUpResult temp = pickTenPickUp();
                if (temp.getFirstAppear() != -1 && result.getFirstAppear() == -1) {
                    result.setFirstAppear(i * 10 + temp.getFirstAppear());
                }
                result.appendResult(temp);
            }
        }

        String words = "";

        words += "一星数量：" + result.getOneNumber() + "\n";
        words += "两星数量：" + result.getTwoNumber() + "\n";
        words += "三星数量：" + result.getThreeNumber() + "\n";
        words += "pick up数量：" + result.getRateUpNumber() + "\n";
        words += "全角色母猪石数量：：" + result.getStoneNumber() + "\n";
        if (result.getFirstAppear() != -1) {
            words += "pick up第一次是在第 " + result.getFirstAppear() + " 抽出现\n";
        } else {
            words += "你井了！！！\n";
        }

        Random random = new Random();
        StringBuilder charList = new StringBuilder();
        if (type == 0) {
            // normal
            int time = result.getThreeNumber();
            ArrayList<String> chars = pcrMapper.getAllCharacter();
            for (int i = 0; i < time - 1; i++) {
                charList.append(chars.get(random.nextInt(chars.size()))).append(", ");
            }
            if (time > 0) {
                charList.append(chars.get(random.nextInt(chars.size()))).append("\n");
            }

        } else if (type == 1) {
            int totalTime = result.getThreeNumber();
            int specialTime = result.getRateUpNumber();

            ArrayList<String> Nchars = pcrMapper.getNormalCharacter();
            ArrayList<String> Schars = pcrMapper.getUpCharacter();
            for (int i = 0; i < totalTime - 1; i++) {
                int dec = random.nextInt(totalTime - i);
                if (dec < specialTime) {
                    charList.append(Schars.get(random.nextInt(Schars.size()))).append(", ");
                    specialTime--;
                } else {
                    charList.append(Nchars.get(random.nextInt(Nchars.size()))).append(", ");
                }

            }
            if (totalTime > 0) {
                if (specialTime > 0) {
                    charList.append(Schars.get(random.nextInt(Schars.size()))).append("\n");
                } else {
                    charList.append(Nchars.get(random.nextInt(Nchars.size()))).append("\n");
                }
            }
        }
        words += charList.toString();
        if (result.getThreeNumber() < 7) {
            words += "\n你也太非了吧， 别抽了，别抽了，入土吧~\n";
        } else if (result.getThreeNumber() < 11) {
            words += "\n符合数学预期水准，知足吧~\n";
        } else {
            words += "\n来人啊，把这只欧洲狗拖出去斩了~\n";
        }
        // System.out.println(words);
        return words;
    }

    public PickUpResult pickTenNormal() {
        int rate = 400;
        double oneStar = 0.795 * rate;
        double twoStar = 0.18 * rate;
        double threeStar = 0.025 * rate;

        Random random = new Random();
        PickUpResult result = new PickUpResult();
        for (int i = 0; i < 9; i++) {
            int num = random.nextInt(rate);
            if (num < oneStar) {
                result.addStoneNumber(1);
                result.addOneNumber(1);
            } else if (num < oneStar + twoStar) {
                result.addStoneNumber(10);
                result.addTwoNumber(1);
            } else if (num < oneStar + twoStar + threeStar) {
                result.addStoneNumber(50);
                result.addThreeNumber(1);
            }
        }

        // 保底
        int num = random.nextInt(rate);
        if (num < threeStar) {
            result.addStoneNumber(50);
            result.addThreeNumber(1);
        } else {
            result.addStoneNumber(10);
            result.addTwoNumber(1);
        }
        return result;
    }

    public PickUpResult pickTenPickUp() {
        int rate = 400;
        double oneStar = 0.795 * rate;
        double twoStar = 0.18 * rate;
        double threeStar = 0.018 * rate;
        double special = 0.007 * rate;


        Random random = new Random();
        PickUpResult result = new PickUpResult();
        for (int i = 0; i < 9; i++) {
            int num = random.nextInt(rate);
            if (num < oneStar) {
                result.addStoneNumber(1);
                result.addOneNumber(1);
            } else if (num < oneStar + twoStar) {
                result.addStoneNumber(10);
                result.addTwoNumber(1);
            } else if (num < oneStar + twoStar + threeStar) {
                result.addStoneNumber(50);
                result.addThreeNumber(1);
            } else if (num < oneStar + twoStar + threeStar + special) {
                result.addStoneNumber(50);
                result.addThreeNumber(1);
                result.addRateUpNumber(1);
                if (result.getFirstAppear() == -1) {
                    result.setFirstAppear(i + 1);
                }
            }
        }

        // 保底
        int num = random.nextInt(rate);
        if (num < threeStar) {
            result.addStoneNumber(50);
            result.addThreeNumber(1);
        } else if (num < threeStar + special) {
            result.addStoneNumber(50);
            result.addThreeNumber(1);
            result.addRateUpNumber(1);
            if (result.getFirstAppear() == -1) {
                result.setFirstAppear(10);
            }
        } else {
            result.addStoneNumber(10);
            result.addTwoNumber(1);
        }
        return result;
    }


    public void sayHi(String user_id) {
        String m = localFileSystem.generateCQForLocalFile(1);
        messageSender.sendMessageToPerson(messageAssemble.assemblePersonalMessage(user_id, m));
    }

}