package com.wyd.BigData
//Kotlin
import com.wyd.BigData.bean.*


import com.wyd.BigData.dao.BaseDao
import com.wyd.BigData.util.SqlUtil
import java.util.*

fun testSavePlayerInfoBatch(){
	val playerInfoList=ArrayList<PlayerInfo>()
	val playerInfo =  PlayerInfo()
	playerInfo.playerId=5
	playerInfo.playerName="kotlin"
	playerInfo.serviceId=1
	playerInfo.channelId=1
	playerInfo.createTime=Date()
	playerInfo.loginTime=playerInfo.createTime
	playerInfoList.add(playerInfo)
	val dao = BaseDao.getInstance()
	//dao.savePlayerInfoBatch(playerInfoList)
	val p=dao.getPlayerInfo(5)
	println(p.playerName)
}
fun testGetLoginInfo(){
}
fun testDoBatch(){
	
}
fun getTableSQL(){
	SqlUtil.getSQLStr("tab_guild_info","test")
}
fun testPlayerLevelInfo(){
    val playerLevelInfo = PlayerLevelInfo()
    playerLevelInfo.channelId=1046
    playerLevelInfo.level=10
    playerLevelInfo.serviceId=666
    playerLevelInfo.playerCount=0
	val dao = BaseDao.getInstance()
	dao.savePlayerLevelInfo(playerLevelInfo)
    var info=dao.getPlayerLevel(666,1046,10)
    println("${info.id} ${info.playerCount}")
    info.playerCount=1
    val infos = ArrayList<PlayerLevelInfo>()
    infos.add(info)
    dao.updatePlayerLevelInfo(playerLevelInfo)
    info=dao.getPlayerLevel(666,1046,10)
    println("${info.id} ${info.playerCount}")
}
fun testUpgradeInfo(){
    val upgradeInfo = UpgradeInfo()
    upgradeInfo.playerLevel=10
    upgradeInfo.serviceId=666
    upgradeInfo.totalCount=0
    upgradeInfo.totalTime=0
    val dao = BaseDao.getInstance()
    dao.saveUpgradeInfo(upgradeInfo)
    var info=dao.getUpgradeInfo(666,10)
    println("${info.id} ${info.totalCount} ${info.totalTime}")
    info.totalCount=1
    val infos = ArrayList<UpgradeInfo>()
    infos.add(info)
    dao.updateUpgradeInfoBatch(infos)
    info=dao.getUpgradeInfo(666,10)
    println("${info.id} ${info.totalCount} ${info.totalTime}")
}
fun testUpdatePlayerInfo(){
    val playerId = 9216604
    val dao = BaseDao.getInstance()
    val playerInfoList = ArrayList<PlayerInfo>()
    val playerInfo = dao.getPlayerInfo(playerId)
    playerInfo.gold=100
    playerInfo.vipLevel=1
    playerInfo.vigor=10
    playerInfo.diamond=120
    playerInfo.totalOnline=140
    playerInfo.guildId=20
    playerInfo.tiroTime=123456
    playerInfo.couresId=5
    playerInfo.couresStep=1
    playerInfoList.add(playerInfo)
    dao.updateVipLevelBatch(playerInfoList)
    dao.updatePlayerUpgradeBatch(playerInfoList)
    dao.updatePlayerLoginInfoBatch(playerInfoList)
    dao.updateTotalOnlineBatch(playerInfoList)
    dao.updatePlayerGuildInfoBatch(playerInfoList)
    dao.updateRechargeBatch(playerInfoList)
    dao.updatePlayerNoviceInfoBatch(playerInfoList)
    val result = dao.getPlayerInfo(playerId)
    print("time:${result.tiroTime} couresId:${result.couresId} couresStep:${result.couresStep}")
}
fun testMarryInfo(){
    val dao = BaseDao.getInstance()
    val marryInfo = MarryInfo()
    marryInfo.serviceId=1
    //dao.saveMarryInfo(marryInfo)
    val list_1 = ArrayList<MarryInfo>()
    val list0 = ArrayList<MarryInfo>()
    val list1 = ArrayList<MarryInfo>()
    val list2 = ArrayList<MarryInfo>()
    val list3 = ArrayList<MarryInfo>()
    val list4 = ArrayList<MarryInfo>()
    val info_1 = dao.getMarryInfo(1)

    info_1.divorceNum=-1
    list_1.add(info_1)
    val info0 = dao.getMarryInfo(1)
    info0.marryNum=10
    list0.add(info0)
    val info1 = dao.getMarryInfo(1)
    info1.luxuriousNum=info1.luxuriousNum+1
    list1.add(info1)
    val info2 = dao.getMarryInfo(1)
    info2.luxuryNum=info2.luxuryNum+2
    list2.add(info2)
    val info3 = dao.getMarryInfo(1)
    info3.romanticNum=3+info3.romanticNum
    list3.add(info3)
    val info4 = dao.getMarryInfo(1)
    info4.generalNum=4+info4.generalNum
    list4.add(info4)

    dao.updateMarryInfoBath(list_1,-1)
    dao.updateMarryInfoBath(list0,0)
    dao.updateMarryInfoBath(list1,1)
    dao.updateMarryInfoBath(list2,2)
    dao.updateMarryInfoBath(list3,3)
    dao.updateMarryInfoBath(list4,4)

}
fun testNoviceInfo(){
    val dao = BaseDao.getInstance()
    val novice = NoviceInfo()
    val list = ArrayList<NoviceInfo>()
    novice.time = (System.currentTimeMillis()/1000).toInt()
    novice.couresId=5
    novice.couresStep=1
    novice.accountId=1001
    novice.playerId=9
    novice.serviceId=666
    list.add(novice)
    dao.saveNoviceInfoBatch(list)
}

fun testDareMapInfoBatch(){
    val cal = Calendar.getInstance()
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    cal.set(Calendar.HOUR_OF_DAY, 0)
    val dao = BaseDao.getInstance()
    val daremap = DareMapInfo()
    val list = ArrayList<DareMapInfo>()
    daremap.recordTime=(System.currentTimeMillis()/1000).toInt()
    daremap.accountId=66
    daremap.action=1
    daremap.challengeType=2
    daremap.difficult=3
    daremap.mapId=4
    daremap.playerId=99
    daremap.serviceId=666
    daremap.time=(cal.timeInMillis/1000).toInt()
    daremap.type=5
    list.add(daremap)
   // dao.saveDareMapInfoBatch(list)
    var  info = dao.getDareMapInfo(666,99,4,2,System.currentTimeMillis())
    println("id:${info.id} action:${info.action}")
    val list2 = ArrayList<DareMapInfo>()
    info.action=2
    list2.add(info)
    dao.updateDareMapActionBath(list2);
    info = dao.getDareMapInfo(666,99,4,2,System.currentTimeMillis())
    println("id:${info.id} action:${info.action}")
}
fun testSingleItemBatch(){
    val dao = BaseDao.getInstance()
    val singleItem = SinglemapItem()
    val list = ArrayList<SinglemapItem>()
    singleItem.startTime=(System.currentTimeMillis()/1000).toInt()
    singleItem.finishTime=1
    singleItem.mapId=5
    singleItem.passStar=2
    singleItem.playerId=99
    list.add(singleItem)
    dao.saveSingleMapItemBatch(list)
    var item =dao.getLastSinglemapItem(99,System.currentTimeMillis())
    item.passStar=3
    item.finishTime=2
    item.dataTime=(System.currentTimeMillis()/1000).toInt()
    val list2 = ArrayList<SinglemapItem>()
    list2.add(item)
    dao.updateSinglemapItemBatch(list2)
    item =dao.getLastSinglemapItem(99,System.currentTimeMillis())
    println("id:${item.id} passStar:${item.passStar}")



}
fun testSinglemapInfo(){
    val dao = BaseDao.getInstance()
    var info = SinglemapInfo()
    info.serviceId=1
    info.mapId=5
      dao.saveSinglemapInfo(info)
    info = dao.getSinglemapInfo(1,5)
    val list = ArrayList<SinglemapInfo>()
    info.dareCount=10
    info.passCount=20
    list.add(info)
    dao.updateSinglemapDareCountBatch(list)
    dao.updateSinglemapPassCountBatch(list)
    info = dao.getSinglemapInfo(1,5)
    print("serviceId:${info.serviceId} mapId:${info.mapId} dareCount:${info.dareCount} passCount:${info.passCount}")


}
fun testTeammapInfo(){
    val dao = BaseDao.getInstance()
    var info = TeammapInfo()
    val list = ArrayList<TeammapInfo>()
    info.serviceId=1
    info.sectionId=2
    list.add(info)
    dao.saveTeammapInfoBatch(list)
    info = dao.getTeammapInfo(1,2)
    println("id:${info.id}")
    list.clear()
    list.add(info)
    info.sMember1Count=1
    dao.updateTeammapInfoBath(list)
    info = dao.getTeammapInfo(1,2)
    println("id:${info.id} smember1count:${info.sMember1Count}")
}
fun testPlayerTeammap(){
    val dao = BaseDao.getInstance()
    var info = PlayerTeammap()
    val list = ArrayList<PlayerTeammap>()
    info.serviceId=1
    info.sectionId=2
    info.playerId =3
    list.add(info)
   // dao.savePlayerTeammapBatch(list)
    info = dao.getPlayerTeammap(3,2)
    println("id:${info.id}")
    list.clear()
    list.add(info)
    info.hDareCount=6
    dao.updatePlayerTeammapBath(list)
    info = dao.getPlayerTeammap(3,2)
    println("id:${info.id} smember1count:${info.hDareCount}")
}
fun main(args: Array<String>) {
    // testMarryInfo()
    // testUpdatePlayerInfo()
    // testNoviceInfo()
    // testDareMapInfoBatch()
    // testSingleItemBatch()
    // testSinglemapInfo()
    // testSingleItemBatch()
    // testTeammapInfo()
    testPlayerTeammap()
}