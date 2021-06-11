package com.crazy.crazyalarm.closeModeActivity.jigsaw.game

interface Game {
    /**
     * 增加难度
     */
    fun addLevel()

    /**
     * 减少难度
     */
    fun reduceLevel()
    //    /**
    //     * 修改游戏模式
    //     */
    //    public void changeMode(String gameMode);
    /**
     * 修改图片
     *
     * @param res
     */
    fun changeImage(res: Int)
}