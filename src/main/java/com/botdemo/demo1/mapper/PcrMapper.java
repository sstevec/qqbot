package com.botdemo.demo1.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.HashMap;

public interface PcrMapper {

    void autoWriteIn(@Param("form_name") String form_name, @Param("column_name") String[] column_name,
                     @Param("array") String[][] new_data);

    void addOneMember(@Param("user_id") String user_id, @Param("card") String card);

    void updateMemberName(@Param("user_id") String user_id, @Param("card") String card);

    HashMap<String, Object> getMemberData(@Param("user_id") String user_id);

    ArrayList<HashMap<String, Object>> getAllMemberData();

    void setMemberData(@Param("user_id") String user_id, @Param("new_data") int new_data);

    void resetAllBattleNumber();


    void addBoss(@Param("boss_number") int boss_number, @Param("boss_health") String boss_health);

    Integer getBossNumber();

    void killBoss(@Param("boss_number") int boss_number);

    void setBossHealthGeneral(@Param("boss_number") int boss_number, @Param("boss_health") String boss_health,@Param("boss_max_health") String boss_max_health);

    HashMap<String, Object> getNextBoss(@Param("boss_number") int boss_number);



    HashMap<String, Object> getCurrentInfo();

    void clearCurrentMember();

    void setCurrentMember(@Param("user_id")String user_id, @Param("card")String card, @Param("combine_code")int combine_code);

    void setCombineMember(@Param("user_id_2")String user_id_2, @Param("card_2")String card_2, @Param("combine_code")int combine_code);

    void setBossHealth(@Param("boss_health") String boss_health);

    void loadNewBoss(@Param("boss_number") int boss_number, @Param("boss_health") String boss_health,@Param("boss_max_health") String boss_max_health);

    ArrayList<String> getAllCharacter();

    ArrayList<String> getUpCharacter();

    ArrayList<String> getNormalCharacter();
}
