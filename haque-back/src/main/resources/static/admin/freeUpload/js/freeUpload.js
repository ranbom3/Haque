;function upload(val_obj) {
    if (typeof val_obj != 'object') {
        console.error('参数必须是object对象');
        return;
    }
    /*文件列表*/
    var files = [];
    var uploadFiles = {
        fileNum: 0, /*上传文件数量*/
        callArr: [], /*返回的数据*/
        error: 0, /*上传错误*/
        fileSize: 0, /*文件总大小*/
        fail: [], /*上传失败的文件*/
        asx: 0/*计数器*/
    };
    var obj = {
        el: '', /*元素*/
        url: '', /*上传地址*/
        type: 'POST', /*上传方式*/
        fileName: 'file[]',
        fileNum: 10, /*文件数*/
        fileSize: 1024 * 4, /*文件大小（kb）*/
        timeout: 1000 * 10, /*上传超时*/
        meanNum: 2,
        fileType:[],/*文件类型*/
        cssUrl:'freeUpload.css',
        success: function (data) {
            /*成功*/
        },
        error: function (data) {
            /*失败*/
        },
        ontime: function (event) {
            /*超时*/
            alert('请求超时！');
        },
        numout: function (num) {
            if (num > this.fileNum)
                alert('文件数量超过限制！');
            else if (num <= 0)
                alert('请选择文件！');
        },
        onfileSize: function () {
            alert('文件大小超过限制');
        },
        repeat: function (val) {
            alert(val.name + "文件重复选择");
        },
        imgClick: function (elem) {
            /*点击图片时的事件*/
            console.log(elem);
        },
        suffClick: function (name) {
            /*点击非图片时的事件*/
            console.log(name);
        },
        onType:function (val) {
            alert(val.name + "文件类型不符");
        }
    };
    if (typeof val_obj == 'object') {
        for (var kay in val_obj) {
            obj[kay] = val_obj[kay];
        }
        if (obj.el == '') {
            console.error("上传文件时的DOM元素不能为空");
            return;
        }
        if (obj.url == '') {
            console.error("文件上传路径必须填写");
            return;
        }
    }
    var dz = document.getElementById(obj.el);
    if (!dz) {
        console.error(obj.el + 'DOM元素没有找到');
        return;
    }
    init();
    if(dz.className.length>0)
        dz.className = dz.className + " daoUpload";
    else
        dz.className = "daoUpload";
    dz.setAttribute('dropEffect', 'link');
    var add = dz.getElementsByClassName('add')[0];
    var uploadDom = dz.getElementsByClassName('uploads')[0];
    var fileSize = dz.getElementsByClassName('file_size')[0];
    add.onclick = function (eve) {
        /*添加文件*/
        var input = dz.getElementsByClassName('infile')[0];
        /*绑定change*/
        var chang_num = 1;
        addEvent(input, 'change', function (event) {
            if (chang_num > 1) return;
            chang_num++;
            if (input.files.length <= 0)return;
            if (uploadFiles.fileNum+input.files.length > obj.fileNum) {
                obj.numout(uploadFiles.fileNum+input.files.length);
                return;
            }
            for (var i = 0; i < input.files.length; i++) {
                /*判断文件类型*/
                if(!fileType(suff(input.files[i].name))){
                    obj.onType(input.files[i]);
                    continue;
                }
                if (input.files[i].size / 1024 > obj.fileSize) {
                    obj.onfileSize(input.files[i].size / 1024);
                    return;
                }
                if (filesSeach(input.files[i].name) >= 0) {
                    obj.repeat(input.files[i]);
                    continue;
                }
                base(input.files[i]);
                files.push(input.files[i]);
                uploadFiles.fileSize += input.files[i].size;
                uploadFiles.fileNum++;
                /*去计算大小*/
                fileadd();
            }
            input.value = '';
        });
        input.click();
    };
    uploadDom.onclick = function (event) {
        if (files.length <= 0) {
            obj.numout(0);
        }
        addSpan();
        setTimeout(function () {
            /*开始上传*/
            var f_len = 0;
            var ls_list = function () {
                if (f_len >= files.length)
                    return;
                var formData = new FormData();
                formData.append(obj.fileName, files[f_len]);
                uploadFile(formData, files[f_len]);
                f_len++;
                setTimeout(ls_list, 0);
            };
            setTimeout(ls_list, 0);
            var timers = null;
            timers = setInterval(function () {
                if (uploadFiles.asx >= files.length) {
                    clearInterval(timers);
                    if (uploadFiles.callArr.length > 0) {
                        obj.success(uploadFiles);
                    }
                    if (uploadFiles.error > 0) {
                        obj.error(uploadFiles);
                    }
                    var str = "成功上传" + uploadFiles.callArr.length + "个文件";
                    if (uploadFiles.error > 0) {
                        str += ",失败" + uploadFiles.fail.length + "个文件。";
                    }
                    fileSize.innerHTML = str;
                    if (uploadFiles.error > 0) {
                        var span = document.createElement('span');
                        span.className = "reup";
                        span.innerText = "重新上传";
                        span.onclick = function (ev) {
                            /*重新上传*/
                            reUpload();
                        };
                        fileSize.appendChild(span);
                    }
                }
            }, 200)

        }, 200)
    };
    var prClassName = dz.className;
    dz.ondragover = function (ev) {
        ev.preventDefault();
        this.className = prClassName + ' over';
    };
    dz.ondragleave = function () {
        this.className = prClassName;
    };
    dz.ondrop = function (ev) {
        this.className = prClassName;
        ev.preventDefault();
        var leng = ev.dataTransfer.files.length;
        if (leng + files.length > obj.fileNum) {
            obj.numout(leng + files.length);
            return;
        }
        for (var i = 0; i < leng; i++) {
            /*判断文件类型*/
            if(!fileType(suff(ev.dataTransfer.files[i].name))){
                obj.onType(ev.dataTransfer.files[i]);
                continue;
            }
            if (ev.dataTransfer.files[i].size / 1024 > obj.fileSize) {
                obj.onfileSize(ev.dataTransfer.files[i] / 1024);
                return;
            }
            if (filesSeach(ev.dataTransfer.files[i].name) >= 0) {
                obj.repeat(ev.dataTransfer.files[i]);
                continue;
            }
            base(ev.dataTransfer.files[i]);
            files.push(ev.dataTransfer.files[i]);
            uploadFiles.fileNum++;
            uploadFiles.fileSize += ev.dataTransfer.files[i].size;
            fileadd();
        }
    };
    /*重新上传*/
    function reUpload() {
        for (var i = 0; i < uploadFiles.fail.length; i++) {
            var lis = dz.getElementsByClassName(uploadFiles.fail[i].name)[0];
            lis.getElementsByClassName('speed')[0].innerText = "正在上传…";
        }
        uploadFiles.error = 0;
        uploadFiles.asx = 0;
        tempFile = uploadFiles.fail;
        uploadFiles.fail = [];
        var f_len = 0;
        var ls_list = function () {
            if (f_len >= tempFile.length)
                return;
            var formData = new FormData();
            formData.append(obj.fileName, tempFile[f_len]);
            uploadFile(formData, tempFile[f_len]);
            f_len++;
            setTimeout(ls_list, 0);
        };
        setTimeout(ls_list, 0);
        var timers = function () {
            if (uploadFiles.asx < f_len)
                setTimeout(timers, 200);
            if (uploadFiles.fail.length <= 0) {
                obj.success(uploadFiles);
            }
            if (uploadFiles.error > 0) {
                obj.error(uploadFiles);
            }
            var str = "成功上传" + uploadFiles.callArr.length + "个文件";
            if (uploadFiles.error > 0) {
                str += ",失败" + uploadFiles.fail.length + "个文件。";
            }
            fileSize.innerHTML = str;
            if (uploadFiles.error > 0) {
                var span = document.createElement('span');
                span.className = "reup";
                span.innerText = "重新上传";
                span.onclick = function (ev) {
                    /*重新上传*/
                    reUpload();
                };
                fileSize.appendChild(span);
            }
        };
        setTimeout(timers, 200)
    }

    /*上传*/
    function uploadFile(formData, val) {
        var dge = 1;
        var temp = dz.getElementsByClassName(val.name)[0];
        var speed = temp.getElementsByClassName('speed')[0];
        var xhr = new XMLHttpRequest();
        xhr.open(obj.type, obj.url, false);
        xhr.send(formData);
        uploadFiles.asx++;
        if (xhr.status == 200) {
            var care = xhr.responseText;
            uploadFiles.callArr.push(care);
            speed.innerText = "上传成功";
        } else {
            uploadFiles.error++;
            dge = 0;
            speed.innerText = "上传失败";
            uploadFiles.fail.push(val);
        }
        return dge;
    }
    function addEvent(el, type, handle) {
        try {
            el.addEventListener(type, handle, false);
        } catch (e) {
            try {
                el.attachEvent('on' + type, handle);
            } catch (e) {
                el['on' + type] = handle;
            }
        }
    }
    /*计算文件大小*/
    function fileadd() {
        var sizeSum = 0;
        for (var s = 0; s < files.length; s++) {
            sizeSum += files[s].size;
        }
        uploadFiles.fileNum = files.length;
        uploadFiles.fileSize = sizeSum;
        sizeSum = sizeSum / 1024;
        if (sizeSum >= 1024) {
            sizeSum = sizeSum / 1024;
            sizeSum = parseInt(sizeSum * 100) / 100;
            fileSize.innerHTML = "选中" + files.length + "个文件，" + "共" + sizeSum + "MB";
        } else if (sizeSum > 0) {
            sizeSum = parseInt(sizeSum * 100) / 100;
            fileSize.innerHTML = "选中" + files.length + "个文件，" + "共" + sizeSum + "KB";
        } else {
            fileSize.innerHTML = "";
        }
    }

    /*文件检索*/
    function filesSeach(name) {
        if (files.length > 0) {
            for (var j = 0; j < files.length; j++) {
                if (name == files[j].name) {
                    return j;
                }
            }
        }
        return -1;
    }

    /*删除文件*/
    function fileDel(name) {
        var pos = filesSeach(name);
        if (pos >= 0) {
            files.splice(pos, 1);
            fileadd();
            return 1;
        }
        return 0;
    }

    /*添加*/
    function addSpan() {
        var lis = dz.getElementsByClassName('li_img');
        for (var i = 0; i < lis.length; i++) {
            var span = document.createElement('span');
            span.className = "speed";
            span.innerText = "正在上传…";
            lis[i].appendChild(span);
            lis[i].getElementsByClassName('del')[0].remove();
        }
        var dom_add = dz.getElementsByClassName('add')[0];
        var dom_uploads = dz.getElementsByClassName('uploads')[0];
        dom_add.onclick = null;
        dom_add.style.backgroundColor = "#7bd5f1";
        dom_add.style.cursor = "not-allowed";
        dom_add.style.hover = "";
        dom_uploads.onclick = null;
        dom_uploads.style.backgroundColor = "#7bd5f1";
        dom_uploads.style.cursor = "not-allowed";
        dom_uploads.style.hover = "";
        return 1;
    }

    /*创建图片*/
    function creatImg(bal, val, suf) {
        var li = document.createElement('li');
        var span = document.createElement('span');
        span.innerText = "删除";
        span.className = "del";
        span.setAttribute('file', bal.name);
        span.onclick = function (ev) {
            imgDel(this);
        };
        if (suf) {
            var sp = document.createElement('span');
            sp.innerText = suf.toUpperCase();
            sp.className = "suff";
            sp.onclick = function () {
                obj.suffClick(bal.name);
            };
            li.appendChild(sp);
        } else {
            var img = document.createElement('img');
            img.setAttribute('src', val);
            img.onclick = function () {
                obj.imgClick(this);
            };
            li.appendChild(img);
        }
        li.setAttribute('file_name', bal.name);
        li.className = "li_img " + bal.name;
        li.onmouseover = function (ev) {
            liOver(this);
        };
        li.onmouseout = function (ev) {
            liOut(this);
        };
        li.appendChild(span);
        var l = dz.getElementsByClassName('imgList')[0];
        l.appendChild(li);
    }

    /*鼠标事件*/
    function liOver(_t) {
        var spa = _t.getElementsByClassName('del')[0];
        if (!spa) {
            return;
        }
        spa.style.display = "inline-block";
    }

    function liOut(_t) {
        var spa = _t.getElementsByClassName('del')[0];
        if (!spa) {
            return;
        }
        spa.style.display = "none";
    }

    /*删除操作*/
    function imgDel(_t) {
        fileDel(_t.getAttribute('file'));
        _t.parentNode.remove();
    }

    /*转换*/
    function base(blod) {
        if (!/image\/\w+/.test(blod.type)) {
            creatImg(blod, '', suff(blod.name));
            return false;
        }
        var reader = new FileReader();
        reader.onload = function (ev) {
            creatImg(blod, this.result);
        };
        reader.readAsDataURL(blod);
    }
    function suff(upFileName) {
        var pos_index = upFileName.lastIndexOf(".");
        return upFileName.substring(pos_index + 1, upFileName.length);
    }
    function fileType(ext) {
        ext = ext.toUpperCase();
        if(obj.fileType.length>0){
            for(var e=0;e<obj.fileType.length;e++){
                if(ext==obj.fileType[e].toUpperCase()){
                    return true;
                }
            }
        }else{
            return true;
        }
        return false;
    }
    function init() {
        dz.innerHTML = "";
        var fileOption = document.createElement('div');
        fileOption.className = "fileOption";
        var size = document.createElement('div');
        size.className = "size";
        var fileSize = document.createElement('span');
        fileSize.className = "file_size";
        var option = document.createElement('div');
        option.className = "option";
        var infile = document.createElement('input');
        infile.className = "infile";
        infile.setAttribute('type', 'file');
        if(obj.fileNum>1)
        infile.setAttribute('multiple', 'multiple');
        infile.style.display = "none";
        var add = document.createElement('span');
        add.className = "add";
        add.innerText = "添加";
        var uploads = document.createElement('span');
        uploads.className = "uploads";
        uploads.innerText = "上传";
        var fileList = document.createElement('div');
        fileList.className = "fileList";
        var imgList = document.createElement('ul');
        imgList.className = "imgList";
        size.appendChild(fileSize);
        option.appendChild(infile);
        option.appendChild(add);
        option.appendChild(uploads);
        fileOption.appendChild(size);
        fileOption.appendChild(option);
        fileList.appendChild(imgList);
        dz.appendChild(fileOption);
        dz.appendChild(fileList);
    }
}