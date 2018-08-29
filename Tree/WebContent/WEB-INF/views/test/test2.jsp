<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title></title>
    <!-- 引入 echarts.js -->
    <script src="<%=basePath%>js/echarts.js"></script>
        <style>
        img {
            width:300px;
            height:300px;
        }
        .gray {
            -webkit-filter: grayscale(100%);
            -moz-filter: grayscale(100%);
            -ms-filter: grayscale(100%);
            -o-filter: grayscale(100%);
            filter: grayscale(100%);
            filter: gray;
        }
        .tran {
            transition:all 0.5s;
        }
        img {
            transition:all 0.5s;
        }
        img:hover {
		    transform: scale(1.2);
		    -webkit-filter: grayscale(0%);
            -moz-filter: grayscale(0%);
            -ms-filter: grayscale(0%);
            -o-filter: grayscale(0%);
            filter: grayscale(0%);
            filter: gray;
		}
    </style>
 </head>
<body style="min-height: 600px">

    <img src="<%=basePath%>images/4.jpg">
    <img src="<%=basePath%>images/4.jpg" class="gray tran ">
<div id="main" style="width: 600px;height:400px;"></div>
<div id="char1" style="width: 600px;height:400px;"></div>
<div id="char2" style="width: 600px;height:400px;"></div>
<div id="char3" style="width: 600px;height:400px;"></div>
<div id="char4" style="width: 600px;height:400px;"></div>
<div id="char6" style="width: 100%;height:1500px;"></div>
    <script type="text/javascript">
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));
        var myChart1 = echarts.init(document.getElementById('char1'));
        var myChart2 = echarts.init(document.getElementById('char2'));
        // 指定图表的配置项和数据
        var option = {
            color: ['#000','#27d44d','#8dc1a9','#ea7e53','#eedd78','#73a373','#0189ff'],
            title: {
                text: 'ECharts 入门示例'
            },
            tooltip: {},
            legend: {
            },
            xAxis: {
                data: ["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
            },
            yAxis: {},
            series: [ {
                type: 'bar',
                name: '2015',
                data: [89.3, 92.1, 94.4, 85.4]
            },
            {
                type: 'bar',
                name: '2016',
                data: [95.8, 89.4, 91.2, 76.9]
            },
            {
                type: 'bar',
                name: '2017',
                data: [97.7, 83.1, 92.5, 78.1]
            }],
            // 高亮样式。
            emphasis: {
                itemStyle: {
                    // 高亮时点的颜色。
                    color: 'blue'
                },
                label: {
                    show: true,
                    // 高亮时标签的文字。
                    formatter: 'This is a emphasis label.'
                }
            }
        };
        
        myChart1.setOption({
            series : [
                {
                    name: '访问来源',
                    type: 'pie',
                    radius: '55%',
                    roseType: 'angle',
                    itemStyle: {
                        // 阴影的大小
                        shadowBlur: 200,
                        // 阴影水平方向上的偏移
                        shadowOffsetX: 0,
                        // 阴影垂直方向上的偏移
                        shadowOffsetY: 0,
                        // 阴影颜色
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    },
                    data:[
                        {value:235, name:'视频广告'},
                        {value:274, name:'联盟广告'},
                        {value:310, name:'邮件营销'},
                        {value:335, name:'直接访问'},
                        {value:400, name:'搜索引擎'}
                    ]
                }
            ]
        })
        //更换背景颜色
        myChart1.setOption({
            backgroundColor: '#2c343c'
        })
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
        //使用dataset创建图标
        option2 = {
        	    legend: {},
        	    tooltip: {},
        	    dataset: {
        	        // 提供一份数据。
        	        source: [
        	            ['product', '2015', '2016', '2017'],
        	            ['Matcha Latte', 43.3, 85.8, 93.7],
        	            ['Milk Tea', 83.1, 73.4, 55.1],
        	            ['Cheese Cocoa', 86.4, 65.2, 82.5],
        	            ['Walnut Brownie', 72.4, 53.9, 39.1]
        	        ]
        	    },
        	    // 声明一个 X 轴，类目轴（category）。默认情况下，类目轴对应到 dataset 第一列。
        	    xAxis: {type: 'category'},
        	    // 声明一个 Y 轴，数值轴。
        	    yAxis: {},
        	    // 声明多个 bar 系列，默认情况下，每个系列会自动对应到 dataset 的每一列。
        	    series: [
        	        {type: 'bar'},
        	        {type: 'bar'},
        	        {type: 'bar'}
        	    ]
        	}
        myChart2.setOption(option2);
        
        var myChart3 = echarts.init(document.getElementById('char3'));
        option3 = {
        	    legend: {},
        	    tooltip: {},
        	    dataset: {
        	        source: [
        	            ['product', '2012', '2013', '2014', '2015'],
        	            ['Matcha Latte', 41.1, 30.4, 65.1, 53.3],
        	            ['Milk Tea', 86.5, 92.1, 85.7, 83.1],
        	            ['Cheese Cocoa', 24.1, 67.2, 79.5, 86.4]
        	        ]
        	    },
        	    xAxis: [
        	        {type: 'category', gridIndex: 0},
        	        {type: 'category', gridIndex: 1}
        	    ],
        	    yAxis: [
        	        {gridIndex: 0},
        	        {gridIndex: 1}
        	    ],
        	    grid: [
        	        {bottom: '55%'},
        	        {top: '55%'}
        	    ],
        	    series: [
        	        // 这几个系列会在第一个直角坐标系中，每个系列对应到 dataset 的每一行。
        	        {type: 'bar', seriesLayoutBy: 'row'},
        	        {type: 'bar', seriesLayoutBy: 'row'},
        	        {type: 'bar', seriesLayoutBy: 'row'},
        	        // 这几个系列会在第二个直角坐标系中，每个系列对应到 dataset 的每一列。
        	        {type: 'bar', xAxisIndex: 1, yAxisIndex: 1},
        	        {type: 'bar', xAxisIndex: 1, yAxisIndex: 1},
        	        {type: 'bar', xAxisIndex: 1, yAxisIndex: 1},
        	        {type: 'bar', xAxisIndex: 1, yAxisIndex: 1}
        	    ]
        	}
        myChart3.setOption(option3);
        
        var option4 = {
        	    dataset: {
        	        source: [
        	            ['score', 'amount', 'product'],
        	            [89.3, 58212, 'Matcha Latte'],
        	            [57.1, 78254, 'Milk Tea'],
        	            [74.4, 41032, 'Cheese Cocoa'],
        	            [50.1, 12755, 'Cheese Brownie'],
        	            [89.7, 20145, 'Matcha Cocoa'],
        	            [68.1, 79146, 'Tea'],
        	            [19.6, 91852, 'Orange Juice'],
        	            [10.6, 101852, 'Lemon Juice'],
        	            [32.7, 20112, 'Walnut Brownie']
        	        ]
        	    },
        	    legend: {},
        	    tooltip: {},
        	    xAxis: {},
        	    yAxis: {type: 'category'},
        	    series: [
        	        {
        	        	type: 'scatter', // 这是个『散点图』
        	            encode: {
        	                // 将 "amount" 列映射到 X 轴。
        	                x: 'amount',
        	                // 将 "product" 列映射到 Y 轴。
        	                y: 'product',
        	                tooltip: [0, 1]
        	            },
        	            symbolSize: function (data) {
        	                return Math.sqrt(data[0]) * 3;
        	            }
        	        }
        	    ],
        	    visualMap: {
        	        orient: 'vertical',
        	        left: '92%',
        	        top: '30%',
        	        min: 0,
        	        max: 100,
        	        text: ['高分', '低分'],
        	        // Map the score column to color
        	        dimension: 0,
        	        inRange: {
        	            color: ['#D7DA8B', '#E15457']
        	        }
        	    },
        	    dataZoom: [
        	               {   // 这个dataZoom组件，默认控制x轴。
        	                   type: 'slider', // 这个 dataZoom 组件是 slider 型 dataZoom 组件
        	                   start: 10,      // 左边在 10% 的位置。
        	                   end: 60         // 右边在 60% 的位置。
        	               },
        	               {   // 这个dataZoom组件，也控制x轴。
        	                   type: 'inside', // 这个 dataZoom 组件是 inside 型 dataZoom 组件
        	                   start: 10,      // 左边在 10% 的位置。
        	                   end: 60         // 右边在 60% 的位置。
        	               }
        	           ]
        	};
        var myChart4 = echarts.init(document.getElementById('char4'));
        myChart4.setOption(option4);
        

        var dataAxis = ['点', '击', '柱', '子', '或', '者', '两', '指', '在', '触', '屏', '上', '滑', '动', '能', '够', '自', '动', '缩', '放'];
        var data = [220, 182, 191, 234, 290, 330, 310, 123, 442, 321, 90, 149, 210, 122, 133, 334, 198, 123, 125, 220];
        var yMax = 500;
        var dataShadow = [];

        for (var i = 0; i < data.length; i++) {
            dataShadow.push(yMax);
        }

        option6 = {
            title: {
                text: '特性示例：渐变色 阴影 点击缩放',
                subtext: 'Feature Sample: Gradient Color, Shadow, Click Zoom'
            },
            xAxis: {
                data: dataAxis,
                axisLabel: {
                    inside: true,
                    textStyle: {
                        color: '#fff'
                    }
                },
                axisTick: {
                    show: false
                },
                axisLine: {
                    show: false
                },
                z: 10
            },
            yAxis: {
                axisLine: {
                    show: false
                },
                axisTick: {
                    show: false
                },
                axisLabel: {
                    textStyle: {
                        color: '#999'
                    }
                }
            },
            dataZoom: [
                {
                    type: 'inside'
                }
            ],
            series: [
                { // For shadow
                    type: 'bar',
                    itemStyle: {
                        normal: {color: 'rgba(0,0,0,0.05)'}
                    },
                    barGap:'-100%',
                    barCategoryGap:'40%',
                    data: dataShadow,
                    animation: false
                },
                {
                    type: 'bar',
                    itemStyle: {
                        normal: {
                            color: new echarts.graphic.LinearGradient(
                                0, 0, 0, 1,
                                [
                                    {offset: 0, color: '#83bff6'},
                                    {offset: 0.5, color: '#188df0'},
                                    {offset: 1, color: '#188df0'}
                                ]
                            )
                        },
                        emphasis: {
                            color: new echarts.graphic.LinearGradient(
                                0, 0, 0, 1,
                                [
                                    {offset: 0, color: '#2378f7'},
                                    {offset: 0.7, color: '#2378f7'},
                                    {offset: 1, color: '#83bff6'}
                                ]
                            )
                        }
                    },
                    data: data
                }
            ]
        };

        // Enable data zoom when user click bar.
        var zoomSize = 6;

        var myChart6 = echarts.init(document.getElementById('char6'));
        myChart6.setOption(option6);
        myChart6.on('click', function (params) {
            console.log(dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)]);
            myChart6.dispatchAction({
                type: 'dataZoom',
                startValue: dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)],
                endValue: dataAxis[Math.min(params.dataIndex + zoomSize / 2, data.length - 1)]
            });
        });

    </script>
</body>
</html>