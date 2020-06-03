<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>第一个 ECharts 实例</title>
    <!-- 引入 echarts.js -->
    <script src="https://cdn.staticfile.org/echarts/4.3.0/echarts.min.js"></script>
    <script src="../js/jquery-1.8.2.min.js"></script>
</head>
<body>
<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
<div id="main" style="width: 600px;height:400px;"></div>

<div id="main1" style="width: 600px;height:400px;"></div>
<script type="text/javascript">
    function init(list){
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('main'));
    var xAxis_data = new Array;
    var series_data = new Array;
    var legend_data = new Array();
    var series_datas = new Array();
    for(var i=0;i<list.length;i++){
        xAxis_data[i]=list[i].goodsname;
        series_data[i]=list[i].num;
        legend_data[i] = list[i].goodsname;
        var map = {
            "value":list[i].price,
            "name":list[i].goodsname
        }
        //map.set(list[i].price,list[i].goodsname);
        series_datas.push(map);
    }
    // 指定图表的配置项和数据
    var option = {
        title: {
            text: '第一个 ECharts 实例'
        },
        tooltip: {},
        legend: {
            data:['销量']
        },
        xAxis: {
            data: xAxis_data//["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
        },
        yAxis: {},
        series: [{
            name: '销量',
            type: 'bar',
            data: series_data
        }]
    };

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);


    var myChart = echarts.init(document.getElementById('main1'));
    option = {
        title : {
            text: '汽车配件销售金额明细',       //大标题
            subtext: '纯属虚构',                //类似于副标题
            x:'center'                 //标题位置   居中
        },
        tooltip : {
            trigger: 'item',           //数据项图形触发，主要在散点图，饼图等无类目轴的图表中使用。
            formatter: "{a} <br/>{b} : {c} ({d}%)"   //{a}（系列名称），{b}（数据项名称），{c}（数值）, {d}（百分比）用于鼠标悬浮时对应的显示格式和内容
        },
        legend: {                           //图例组件。
            orient: 'vertical',             //图例列表的布局朝向
            left: 'left',
            data: legend_data//['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']
        },
        series : [              //系列列表。每个系列通过 type 决定自己的图表类型
            {
                name: '访问来源',
                type: 'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:series_datas/*[
                    {value:335, name:'直接访问'},
                    {value:310, name:'邮件营销'},
                    {value:234, name:'联盟广告'},
                    {value:135, name:'视频广告'},
                    {value:1548, name:'搜索引擎'}
                ]*/,
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
    myChart.setOption(option);
    }
    window.onload = function() {
        var list = new Array;
        list = getList();

        // 方法体

        init(list);
    }
    function getList() {
        var nameList=new Array;
        $.ajax({
            type:"post",
            async:false,
            url:"${pageContext.request.contextPath }/cargoods/queryListNew",
            success:function(data) {
                nameList = data;
                return nameList;
            }
        });
        return nameList;
    }
</script>
</body>
</html>