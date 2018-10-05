//饼状图
(function(){

    var pie1 = echarts.init(document.getElementById("pie1"));

    option = {
        title : {
            text: '图书情况',
            x:'center'
        },
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            data: ['可使用','被预约','被借出']
        },
        series : [
            {
                name: '图书情况',
                type: 'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:[
                    {value:[[${availableNumber}]], name:'在馆内'},
                    {value:[[${reserveNumber}]], name:'被预约'},
                    {value:[[${borrowedNumber}]], name:'已借出'}
                    // {value:0,name:'可使用'},
                    // {value:0,name:'被预约'},
                    // {value:0,name:'被借出'}
                ],
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };

    pie1.setOption(option);
})();

(function(){

    var myChart = echarts.init(document.getElementById("Stack"));

    option = {
        title: {
            text: '图书出入情况'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data:['借阅','归还']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: ['周一','周二','周三','周四','周五','周六','周日']
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name:'借阅',
                type:'line',
                stack: '总量',
                data:[120, 132, 101, 134, 90, 230, 210]
            },
            {
                name:'归还',
                type:'line',
                stack: '总量',
                data:[220, 182, 191, 234, 290, 330, 310]
            }
        ]
    };

    myChart.setOption(option);
})();