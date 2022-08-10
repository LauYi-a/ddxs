#!/bin/bash
cd `dirname $0`
cd ..
BASE_DIR=`pwd`
# 创建定时任务文件
CRON_COUNT=`crontab -l| grep -c "$BASE_DIR/bin/log-crontab.sh"`
if [ $CRON_COUNT -eq 0 ];then
        crontab -l >> $BASE_DIR/bin/cron.everyday
        echo "0 0 */2 * * $BASE_DIR/bin/log-crontab.sh" >> $BASE_DIR/bin/cron.everyday
        crontab $BASE_DIR/bin/cron.everyday
        rm -rf $BASE_DIR/bin/cron.everyday
fi
nowDate=`date +%Y-%m-%d`
oldDate=`date +%Y-%m -d -1month`
yesterdayDate=`date +%Y-%m-%d -d -1day`
function exe_dependencies()
{
    cp $BASE_DIR/log/stdout.log $BASE_DIR/log/log-${yesterdayDate}.log
	echo >> $BASE_DIR/log/stdout.log
	rm -rf $BASE_DIR/log/log-${oldDate}*
}
exe_dependencies