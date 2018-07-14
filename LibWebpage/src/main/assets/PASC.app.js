 (function (root, factory) {
  if (typeof define === 'function' && define.amd) {
  define(function() {
         return factory(root);
         });
  } else if (typeof exports === 'object') {
  module.exports = factory;
  } else {
  root.PASC = factory(root);
  }
  })(this, function (root) {
     
     'use strict';
     
     var PASC = {
     app:{}
     };
          
     var setupWebViewJavascriptBridge = function(callback) {
     if (window.WebViewJavascriptBridge) {
     callback(WebViewJavascriptBridge)
     } else {
     document.addEventListener(
                               'WebViewJavascriptBridgeReady'
                               , function() {
                               callback(WebViewJavascriptBridge)
                               },
                               false
                               );
     }
     
     if (window.WVJBCallbacks) { return window.WVJBCallbacks.push(callback); }
     window.WVJBCallbacks = [callback];
     var WVJBIframe = document.createElement('iframe');
     WVJBIframe.style.display = 'none';
     WVJBIframe.src = 'wvjbscheme://__BRIDGE_LOADED__';
     document.documentElement.appendChild(WVJBIframe);
     setTimeout(function() { document.documentElement.removeChild(WVJBIframe) }, 0)
     }
     
     //0
     PASC.app.isSupportNativeApi = function (opts, success, error) {
     opts = opts || {};
     setupWebViewJavascriptBridge(function(bridge) {
                                  bridge.callHandler('PASC.app.isSupportNativeApi',opts,function(resp){
                                                     var result;
                                                                     if(resp && resp.code) {
                                                                         result = resp;
                                                                     } else {
                                                                         result = JSON.parse(resp);
                                                                     }
                                                                 if(result.code === 0) {
                                                                     success(result);
                                                                 } else {
                                                                     error(result);
                                                                 }
                                                     });
                                  });
     }
     //1
     PASC.app.isSupportNativeApis = function (opts, success, error) {
     opts = opts || {};
     setupWebViewJavascriptBridge(function(bridge) {
                                  bridge.callHandler('PASC.app.isSupportNativeApis',opts,function(resp){
                                                    var result;
                                                                    if(resp && resp.code) {
                                                                        result = resp;
                                                                    } else {
                                                                        result = JSON.parse(resp);
                                                                    }
                                                                if(result.code === 0) {
                                                                    success(result);
                                                                } else {
                                                                    error(result);
                                                                }
                                                     });
                                  });
     }
     
     //2
     PASC.app.openWebView = function (opts, success, error) {
     opts = opts || {};
     setupWebViewJavascriptBridge(function(bridge) {
                                  bridge.callHandler('PASC.app.openWebView',opts,function(resp){
                                                     var result;
                                                                     if(resp && resp.code) {
                                                                         result = resp;
                                                                     } else {
                                                                         result = JSON.parse(resp);
                                                                     }
                                                                 if(result.code === 0) {
                                                                     success(result);
                                                                 } else {
                                                                     error(result);
                                                                 }
                                                     });
                                  });
     }

     //3
     PASC.app.webControlBackItem = function (opts, success, error) {
     opts = opts || {};
     setupWebViewJavascriptBridge(function(bridge) {
                                  bridge.registerHandler(opts.actionName,function(data,responseCallback){
                                                         alert("客户端了返回按钮，确定退出webview？");
                                                         var options = {}
                                                         PASC.app.exitWebView(options, function(res){

                                                                              }, function(err) {
                                                                              alert("callback error="+JSON.stringify(err));
                                                                              })
                                                         });

                                  bridge.callHandler('PASC.app.webControlBackItem',opts,function(resp){
                                                     var result;
                if(resp && resp.code) {
                    result = resp;
                } else {
                    result = JSON.parse(resp);
                }
            if(result.code === 0) {
                success(result);
            } else {
                error(result);
            }
                                                     });
                                  });
     }

     //4
    PASC.app.exitWebView = function (opts, success, error) {
     opts = opts || {};
     setupWebViewJavascriptBridge(function(bridge) {
                                  bridge.callHandler('PASC.app.exitWebView',opts,function(resp){
                                                     var result;
                if(resp && resp.code) {
                    result = resp;
                } else {
                    result = JSON.parse(resp);
                }
            if(result.code === 0) {
                success(result);
            } else {
                error(result);
            }
                                                     });
                                  });
     }

     //5
     PASC.app.webViewGoback = function (opts, success, error) {
     opts = opts || {};
     setupWebViewJavascriptBridge(function(bridge) {
                                  bridge.callHandler('PASC.app.webViewGoback',opts,function(resp){
                                                     var result;
                if(resp && resp.code) {
                    result = resp;
                } else {
                    result = JSON.parse(resp);
                }
            if(result.code === 0) {
                success(result);
            } else {
                error(result);
            }
                                                     });
                                  });
     }

     //6
     PASC.app.setNavigationBar = function (opts, success, error) {
     opts = opts || {};
     setupWebViewJavascriptBridge(function(bridge) {
                                  bridge.registerHandler(opts.actionName,function(data,responseCallback){
                                                         alert("客户端点击了按钮");
                                                         });

                                  bridge.callHandler('PASC.app.setNavigationBar',opts,function(resp){
                                                     var result;
                if(resp && resp.code) {
                    result = resp;
                } else {
                    result = JSON.parse(resp);
                }
            if(result.code === 0) {
                success(result);
            } else {
                error(result);
            }
                                                     });
                                  });
     }

     //7
     PASC.app.backToRootView = function (opts, success, error) {
     opts = opts || {};
     setupWebViewJavascriptBridge(function(bridge) {
                                  bridge.callHandler('PASC.app.backToRootView',opts,function(resp){
                                                     var result;
                if(resp && resp.code) {
                    result = resp;
                } else {
                    result = JSON.parse(resp);
                }
            if(result.code === 0) {
                success(result);
            } else {
                error(result);
            }
                                                     });
                                  });
     }

     //8
     PASC.app.selectTabBar = function (opts, success, error) {
     opts = opts || {};
     setupWebViewJavascriptBridge(function(bridge) {
                                  bridge.callHandler('PASC.app.selectTabBar',opts,function(resp){
                                                     var result;
                if(resp && resp.code) {
                    result = resp;
                } else {
                    result = JSON.parse(resp);
                }
            if(result.code === 0) {
                success(result);
            } else {
                error(result);
            }
                                                     });
                                  });
     }

     //9
     PASC.app.previewImages = function (opts, success, error) {
     opts = opts || {};
     setupWebViewJavascriptBridge(function(bridge) {
                                  bridge.callHandler('PASC.app.previewImages',opts,function(resp){
                                                     var result;
                if(resp && resp.code) {
                    result = resp;
                } else {
                    result = JSON.parse(resp);
                }
            if(result.code === 0) {
                success(result);
            } else {
                error(result);
            }
                                                     });
                                  });
     }

     //10
     PASC.app.map = function (opts, success, error) {
     opts = opts || {};
     setupWebViewJavascriptBridge(function(bridge) {
                                  bridge.callHandler('PASC.app.map',opts,function(resp){
                                                     var result;
                if(resp && resp.code) {
                    result = resp;
                } else {
                    result = JSON.parse(resp);
                }
            if(result.code === 0) {
                success(result);
            } else {
                error(result);
            }
                                                     });
                                  });
     }

     //11
     PASC.app.statisticsPage = function (opts, success, error) {
     opts = opts || {};
     setupWebViewJavascriptBridge(function(bridge) {
                                  bridge.callHandler('PASC.app.statisticsPage',opts,function(resp){
                                                     var result;
                if(resp && resp.code) {
                    result = resp;
                } else {
                    result = JSON.parse(resp);
                }
            if(result.code === 0) {
                success(result);
            } else {
                error(result);
            }
                                                     });
                                  });
     }

     //12
     PASC.app.statisticsEvent = function (opts, success, error) {
     opts = opts || {};
     setupWebViewJavascriptBridge(function(bridge) {
                                  bridge.callHandler('PASC.app.statisticsEvent',opts,function(resp){
                                                     var result;
                if(resp && resp.code) {
                    result = resp;
                } else {
                    result = JSON.parse(resp);
                }
            if(result.code === 0) {
                success(result);
            } else {
                error(result);
            }
                                                     });
                                  });
     }

     //13
     PASC.app.share = function (opts, success, error) {
     opts = opts || {};
     setupWebViewJavascriptBridge(function(bridge) {
                                  bridge.callHandler('PASC.app.share',opts,function(resp){
                                                     var result;
                if(resp && resp.code) {
                    result = resp;
                } else {
                    result = JSON.parse(resp);
                }
            if(result.code === 0) {
                success(result);
            } else {
                error(result);
            }
                                                     });
                                  });
     }

     //14
     PASC.app.log = function (opts, success, error) {
     opts = opts || {};
     setupWebViewJavascriptBridge(function(bridge) {
                                  bridge.callHandler('PASC.app.log',opts,function(resp){
                                                     var result;
                if(resp && resp.code) {
                    result = resp;
                } else {
                    result = JSON.parse(resp);
                }
            if(result.code === 0) {
                success(result);
            } else {
                error(result);
            }
                                                     });
                                  });
     }

     //15
     PASC.app.getDeviceInfo = function (opts, success, error) {
     opts = opts || {};
     setupWebViewJavascriptBridge(function(bridge) {
                                  bridge.callHandler('PASC.app.getDeviceInfo',opts,function(resp){
                                                     var result;
                if(resp && resp.code) {
                    result = resp;
                } else {
                    result = JSON.parse(resp);
                }
            if(result.code === 0) {
                success(result);
            } else {
                error(result);
            }
                                                     });
                                  });
     }

     //16
     PASC.app.boxTips = function (opts, success, error) {
     opts = opts || {};
     setupWebViewJavascriptBridge(function(bridge) {
                                  bridge.callHandler('PASC.app.boxTips',opts,function(resp){
                                                     var result;
                if(resp && resp.code) {
                    result = resp;
                } else {
                    result = JSON.parse(resp);
                }
            if(result.code === 0) {
                success(result);
            } else {
                error(result);
            }
                                                     });
                                  });
     }

     //17
     PASC.app.takePhoto = function (opts, success, error) {
     opts = opts || {};
     setupWebViewJavascriptBridge(function(bridge) {
                                  bridge.callHandler('PASC.app.takePhoto',opts,function(resp){
                                                     var result;
                if(resp && resp.code) {
                    result = resp;
                } else {
                    result = JSON.parse(resp);
                }
            if(result.code === 0) {
                success(result);
            } else {
                error(result);
            }
                                                     });
                                  });
     }

     //18
     PASC.app.photoAlbum = function (opts, success, error) {
     opts = opts || {};
     setupWebViewJavascriptBridge(function(bridge) {
                                  bridge.callHandler('PASC.app.photoAlbum',opts,function(resp){
                                                     var result;
                if(resp && resp.code) {
                    result = resp;
                } else {
                    result = JSON.parse(resp);
                }
            if(result.code === 0) {
                success(result);
            } else {
                error(result);
            }
                                                     });
                                  });
     }

     //19
     PASC.app.selectPhoto = function (opts, success, error) {
     opts = opts || {};
     setupWebViewJavascriptBridge(function(bridge) {
                                  bridge.callHandler('PASC.app.selectPhoto',opts,function(resp){
                                                     var result;
                if(resp && resp.code) {
                    result = resp;
                } else {
                    result = JSON.parse(resp);
                }
            if(result.code === 0) {
                success(result);
            } else {
                error(result);
            }
                                                     });
                                  });
     }

     //20
     PASC.app.selectContact = function (opts, success, error) {
     opts = opts || {};
     setupWebViewJavascriptBridge(function(bridge) {
                                  bridge.callHandler('PASC.app.selectContact',opts,function(resp){
                                                     var result;
                if(resp && resp.code) {
                    result = resp;
                } else {
                    result = JSON.parse(resp);
                }
            if(result.code === 0) {
                success(result);
            } else {
                error(result);
            }
                                                     });
                                  });
     }

     //21
     PASC.app.gps = function (opts, success, error) {
     opts = opts || {};
     setupWebViewJavascriptBridge(function(bridge) {
                                  bridge.callHandler('PASC.app.gps',opts,function(resp){
                                                     var result;
                if(resp && resp.code) {
                    result = resp;
                } else {
                    result = JSON.parse(resp);
                }
            if(result.code === 0) {
                success(result);
            } else {
                error(result);
            }
                                                     });
                                  });
     }

     //22
     PASC.app.scan = function (opts, success, error) {
     opts = opts || {};
     setupWebViewJavascriptBridge(function(bridge) {
                                  bridge.callHandler('PASC.app.scan',opts,function(resp){
                                                     var result;
                if(resp && resp.code) {
                    result = resp;
                } else {
                    result = JSON.parse(resp);
                }
            if(result.code === 0) {
                success(result);
            } else {
                error(result);
            }
                                                     });
                                  });
     }

     //23
     PASC.app.memoryCache = function (opts, success, error) {
     opts = opts || {};
     setupWebViewJavascriptBridge(function(bridge) {
                                  bridge.callHandler('PASC.app.memoryCache',opts,function(resp){
                                                     var result;
                if(resp && resp.code) {
                    result = resp;
                } else {
                    result = JSON.parse(resp);
                }
            if(result.code === 0) {
                success(result);
            } else {
                error(result);
            }
                                                     });
                                  });
     }

     //24
     PASC.app.diskCache = function (opts, success, error) {
     opts = opts || {};
     setupWebViewJavascriptBridge(function(bridge) {
                                  bridge.callHandler('PASC.app.diskCache',opts,function(resp){
                                                     var result;
                if(resp && resp.code) {
                    result = resp;
                } else {
                    result = JSON.parse(resp);
                }
            if(result.code === 0) {
                success(result);
            } else {
                error(result);
            }
                                                     });
                                  });
     }

     //25
     PASC.app.installedSharePlatforms = function (opts, success, error) {
     opts = opts || {};
     setupWebViewJavascriptBridge(function(bridge) {
                                  bridge.callHandler('PASC.app.installedSharePlatforms',opts,function(resp){
                                                     var result;
                if(resp && resp.code) {
                    result = resp;
                } else {
                    result = JSON.parse(resp);
                }
            if(result.code === 0) {
                success(result);
            } else {
                error(result);
            }
                                                     });
                                  });
     }
     
     return PASC;
     });
