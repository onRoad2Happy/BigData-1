package com.wyd.BigData.util;
/**
 * Created by root on 6/7/17.
 */
public class DataType {
    /** 标识号-服务启动 */
    public static final int MARKNUM_START          = 0;
    /** 标识号-创建角色 */
    public static final int MARKNUM_CREATE         = 1;
    /** 标识号-角色登入 */
    public static final int MARKNUM_LOGIN          = 2;
    /** 标识号-角色登出 */
    public static final int MARKNUM_LOGOUT         = 3;
    /** 标识号-角色充值 */
    public static final int MARKNUM_RECHARGE       = 4;
    /** 标识号-角色在线 */
    public static final int MARKNUM_ONLINENUM      = 5;
    /** 标识号-角色升级 */
    public static final int MARKNUM_PLAYERUPGRADE  = 6;
    /** 标识号-VIP升级 */
    public static final int MARKNUM_VIPUPGRADE     = 7;
    /** 标识号-创建公会 */
    public static final int MARKNUM_CREATEGUILD    = 8;
    /** 标识号-公会升级 */
    public static final int MARKNUM_GUILDUPGRADE   = 9;
    /** 标识号-加入公会 */
    public static final int MARKNUM_JOINGUILD      = 10;
    /** 标识号-退出公会 */
    public static final int MARKNUM_QUITGUILD      = 11;
    /** 标识号-角色结婚 */
    public static final int MARKNUM_MARRY          = 12;
    /** 标识号-角色离婚 */
    public static final int MARKNUM_DIVORCE        = 13;
    /** 标识号-新手教程 */
    public static final int MARKNUM_NOVICE         = 14;
    /** 标识号-触发任务 */
    public static final int MARKNUM_GETTASK        = 15;
    /** 标识号-完成任务 */
    public static final int MARKNUM_ENDTASK        = 16;
    /** 标识号-挑战单人副本 */
    public static final int MARKNUM_DARESINGLE     = 17;
    /** 标识号-通关单人副本 */
    public static final int MARKNUM_PASSSINGLE     = 18;
    /** 标识号-物品流水 */
    public static final int MARKNUM_ITEMLOG        = 19;
    /** 标识号-挑战组队副本 */
    public static final int MARKNUM_DARETEAMMAP    = 20;
    /** 标识号-组队副本钻石翻牌 */
    public static final int MARKNUM_TEAMMAPLOTTERY = 21;//合并到MARKNUM_TEAMMAPLOTTERY_ITEM
    /** 标识号-组队副本重置 */
    public static final int MARKNUM_TEAMMAPRESET   = 22;// 合并到MARKNUM_TEAMMAPRESET_ITEM
    /** 标识号-竞技场日志 */
    public static final int MARKNUM_BATTLELOG      = 23;
    /** 标识号-玩家聊天日志 */
    public static final int MARKNUM_CHATLOG        = 24;
    /** 标识号-装备强化日志 */
    public static final int MARKNUM_STRENGTH       = 25;
    /** 标识号-装备升星日志 */
    public static final int MARKNUM_UPSTAR        	= 26;
    /** 标识号-装备继承日志 */
    public static final int MARKNUM_INHERIT        = 27;
    /** 标识号-完成成就日志 */
    public static final int MARKNUM_ACHIEVEMENT    = 28;
    /** 标识号-宠物砸蛋 */
    public static final int MARKNUM_GETPET    		= 29;
    /** 标识号-宠物升级 */
    public static final int MARKNUM_PETUPGRADE    	= 30;
    /** 标识号-宠物进阶 */
    public static final int MARKNUM_PETADVANCE   	= 31;
    /** 标识号-宠物重生 */
    public static final int MARKNUM_PETREBIRTH    	= 32;
    /** 标识号-坐骑升级 */
    public static final int MARKNUM_MOUNTSUPGRADE  = 33;
    /** 标识号-坐骑进阶 */
    public static final int MARKNUM_MOUNTSADVANCE  = 34;
    /** 标识号-武器洗练 */
    public static final int MARKNUM_WASH  = 35;
    /** 标识号-竞技商店 */
    //    public static final int MARKNUM_WICTORY_STORE  = 36;
    /** 标识号-竞技赛段位升级 */
    public static final int MARKNUM_ARENA_UPGRADE  = 36;
    /** 标识号-意见箱意见回复 */
    public static final int MARKNUM_SUGGESTIONBOX  = 37;
    /** 标识号-竞技匹配 */
    public static final int MARKNUM_MARKPAIR  = 38;
    /** 标识号-装备抽奖 */
    public static final int MARKNUM_GETEQUIP  = 39;
    /** 标识号-夫妻一起战斗 */
    public static final int MARKNUM_FQZD        = 40;
    /** 标识号-公会成员一起战斗 */
    public static final int MARKNUM_GUILDZD     = 41;
    /** 标识号-禁言 */
    public static final int MARKNUM_GAGY        = 42;
    /** 标识号-解禁 */
    public static final int MARKNUM_GAGN        = 43;
    /** 标识号-祈福 */
    public static final int MARKNUM_BLESS_GET   = 44;
    /** 标识号-祈福石吞噬 */
    public static final int MARKNUM_BLESS_DEVOUR = 45;
    /** 标识号-发布公会任务 */
    public static final int MARKNUM_GUILD_TASK_PUBLISH	= 46;
    /** 标识号-完成公会任务 */
    public static final int MARKNUM_GUILD_TASK_FINISH	= 47;
    /** 爬塔副本开始挑战 */
    public static final int MARKNUM_TOWER_START		= 50;
    /** 爬塔副本完成挑战 */
    public static final int MARKNUM_TOWER_FINISH		= 51;
    /** 秘境宝藏开始挑战 */
    public static final int MARKNUM_SECRET_START		= 52;
    /** 秘境宝藏完成挑战 */
    public static final int MARKNUM_SECRET_FINISH		= 53;
    /** 世界boss开始挑战 */
    public static final int MARKNUM_BOSS_START		    = 54;
    /** 世界boss鼓舞 */
    public static final int MARKNUM_BOSS_INSPIRE	    = 55;
    /** 世界boss重置挑战 */
    public static final int MARKNUM_BOSS_RESET	        = 56;
    /** 竞技场 */
    public static final int MARKNUM_BATTLE_ARENA	    = 57;
    /** 活动 */
    public static final int MARKNUM_ACTIVITY	        = 58;
    /** 标识号-修炼 */
    public static final int MARKNUM_UPGRADE      		= 59;
    /** 标识号-卡牌 */
    public static final int MARKNUM_CARD      			= 60;
    /** 标识号-组队副本钻石翻牌 */
    public static final int MARKNUM_TEAMMAPLOTTERY_ITEM	= 61;
    /** 标识号-组队副本重置 */
    public static final int MARKNUM_TEAMMAPRESET_ITEM   = 62;

    /** 标识号-添加、删除好友 */
    public static final int MARKNUM_FRIEND  			= 63;
    /** 标识号-穿卸装备日志 */
    public static final int MARKNUM_EQUIPMENT_WEAR  	= 64;
    /** 标识号-怒火升级 */
    public static final int MARKNUM_PROP_UPGRADE  		= 65;
    /** 标识号-爬塔副本重置 */
    public static final int MARKNUM_TOWER_RESET        	= 66;
    /** 标识号-宠物出战 */
    public static final int MARKNUM_PET_FIGHT       	= 67;
    /** 标识号-排位赛 */
    public static final int MARKNUM_QUALIFYING       	= 68;
    /** 标识号-穿戴装备强化 */
    public static final int MARKNUM_EQUIPMENT_STRENGTH 	= 69;
    /** 标识号-道具激活 */
    public static final int MARKNUM_PROP_ACTIVATE  		= 70;
    /** 标识号-宠物休息 */
    public static final int MARKNUM_PET_REST	  		= 71;
    /** 标识号-制作紫装 */
    public static final int MARKNUM_ADVANCED_PURPLE	  	= 72;
    /** 标识号-制作橙装 */
    public static final int MARKNUM_ADVANCED_ORANGE	  	= 74;
    /** 标识号-单人副本扫荡 */
    public static final int MARKNUM_SAODANG_SINGLEMAP	= 76;

    /** 标识号-许愿池 */
    public static final int MARKNUM_WISHING_WELL		= 95;

    /** 标识号-排位赛 */
    public static final int MARKNUM_RANK_MATCH			= 98;
    /** 标识号-语音聊天 */
    public static final int MARKNUM_VOICE_CHAT			= 99;
    ////////////////////////////////	运营数据上传类型		/////////////////////////////////
    /** 运营数据标识号 - 用户信息 */
    public static final int UPLOADFILE_USERINFO = 1;
    /** 运营数据标识号 - 充值数据 */
    public static final int UPLOADFILE_CHARGE = 2;
    /** 运营数据标识号 - 实时数据 */
    public static final int UPLOADFILE_REALTIME = 3;
    /** 运营数据标识号 - 行为数据 */
    public static final int UPLOADFILE_EVENT = 4;
}
