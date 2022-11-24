<template>
  <basic-container>
    <avue-crud :option="option"
               :search.sync="search"
               :table-loading="loading"
               :data="data"
               :page="page"
               :permission="permissionList"
               :before-open="beforeOpen"
               v-model="form"
               ref="crud"
               @row-update="rowUpdate"
               @row-save="rowSave"
               @row-del="rowDel"
               @search-change="searchChange"
               @search-reset="searchReset"
               @selection-change="selectionChange"
               @current-change="currentChange"
               @size-change="sizeChange"
               @refresh-change="refreshChange"
               @on-load="onLoad">
      <template slot="menuLeft">
        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain
                   v-if="permission.question_delete"
                   @click="handleDelete">删 除
        </el-button>
        <el-button type="success"
                   size="small"
                   plain
                   icon="el-icon-upload2"
                   @click="handleImport">导入
        </el-button>
      </template>
      <template slot-scope="scope" slot="subjectIdForm">
        <el-cascader
          placeholder="专业分类"
          :options="subjectExamTreeList"
          v-model="form.subjectId"
          :props="{'label':'title','emitPath':false}"
          filterable></el-cascader>
      </template>
      <template slot-scope="scope" slot="qstContentSearch">
        <el-input
          type="text"
          v-model="search.qstContent"
          placeholder="试题题干"
         ></el-input>
      </template>
      <template slot-scope="scope" slot="qstContentForm">
        <avue-ueditor v-model="form.qstContent"  :options="ueditorOptions2" style="width:90%;"></avue-ueditor>
      </template>
      <template slot-scope="scope" slot="qstAnalyzeForm">
        <avue-ueditor v-model="form.qstAnalyze"  :options="ueditorOptions2" style="width:90%;"></avue-ueditor>
      </template>
      <template slot-scope="scope" slot="optionListForm">
        <div>
          <!-- 单选题 -->
          <el-row v-if="form.qstType == 1" v-for="(item,index) of optionArr" :key="index" type="flex">
            <el-col :span="23"  >
              <div v-html="optionArr[index].value"  @click="openEditor(index)" v-if="!optionArr[index].editor" placeholder="请输入选项" style="line-height: 32px;border-radius: 4px;border: 1px solid #DCDFE6;min-height:32px;width:90%;"></div>
                <avue-ueditor v-model="optionArr[index].value" v-if="optionArr[index].editor" :options="ueditorOptions" style="width:90%;"></avue-ueditor>

<!--                <el-button @click="optionPlus" v-if="index == 0" type="primary" icon="el-icon-plus" circle style="margin-left: 5px"></el-button>-->
<!--                <el-button @click="optionMinus(index)"  v-if="index > 0" type="danger" icon="el-icon-minus" circle style="margin-left: 5px"></el-button>-->
            </el-col>
            <el-col :span="1" >
              <!--                <el-input v-model="optionArr[index].value" placeholder="请输入选项" style="width:90%;"></el-input>-->
<!--              <avue-ueditor v-model="optionArr[index].value" :options="ueditorOptions" style="width:90%;"></avue-ueditor>-->

              <el-button @click="optionPlus" v-if="index == 0" type="primary" icon="el-icon-plus" circle style="margin-left: 5px"></el-button>
              <el-button @click="optionMinus(index)"  v-if="index > 2" type="danger" icon="el-icon-minus" circle style="margin-left: 5px"></el-button>
            </el-col>
          </el-row>
          <!-- 多选题 -->
          <el-row v-for="(item,index) of optionArr" :key="index" v-if="form.qstType == 2" type="flex">
            <el-col :span="23"  >
              <div v-html="optionArr[index].value"  @click="openEditor(index)" v-if="!optionArr[index].editor" placeholder="请输入选项" style="line-height: 32px;border-radius: 4px;border: 1px solid #DCDFE6;min-height:32px;width:90%;"></div>
<!--              <el-input v-model="optionArr[index].value" @focus="openEditor(index)" v-if="!optionArr[index].editor" placeholder="请输入选项" style="width:90%;"></el-input>-->
              <avue-ueditor v-model="optionArr[index].value" v-if="optionArr[index].editor" :options="ueditorOptions" style="width:90%;"></avue-ueditor>
<!--              <el-button @click="optionPlus" v-if="index == 0" type="primary" icon="el-icon-plus" circle style="margin-left: 5px"></el-button>-->
<!--              <el-button @click="optionMinus(index)"  v-if="index > 0" type="danger" icon="el-icon-minus" circle style="margin-left: 5px"></el-button>-->
            </el-col>
            <el-col :span="1"  >
              <!--              <el-input v-model="optionArr[index].value" placeholder="请输入选项" style="width:90%;"></el-input>-->
<!--              <avue-ueditor v-model="optionArr[index].value" :options="ueditorOptions" style="width:90%;"></avue-ueditor>-->
              <el-button @click="optionPlus" v-if="index == 0" type="primary" icon="el-icon-plus" circle style="margin-left: 5px"></el-button>
              <el-button @click="optionMinus(index)"  v-if="index > 2" type="danger" icon="el-icon-minus" circle style="margin-left: 5px"></el-button>
            </el-col>
          </el-row>
          <!-- 判断题 -->
          <el-row v-if="form.qstType == 3">
            <el-col :span="24" >
              <el-radio-group v-model="isAsr">
                <el-radio-button label="A" >正确</el-radio-button>
                <el-radio-button label="B">错误</el-radio-button>
              </el-radio-group>
            </el-col>
          </el-row>
          <!-- 填空题 -->
          <el-row v-if="form.qstType == 4">
            <el-col :span="23"  v-for="(item,index) of optionArr" :key="index">
             <!-- <div v-html="optionArr[index].value"  @click="openEditor(index)" v-if="!optionArr[index].editor" placeholder="请输入选项" style="line-height: 32px;border-radius: 4px;border: 1px solid #DCDFE6;min-height:32px;width:90%;"></div>-->
             <el-input v-model="optionArr[index].value" v-if="!optionArr[index].editor" placeholder="请输入选项" style="width:90%;"></el-input>
              <!--               <avue-ueditor v-model="optionArr[index].value" v-if="optionArr[index].editor" :options="ueditorOptions" style="width:90%;"></avue-ueditor>-->
<!--              <el-button @click="optionPlus" v-if="index == 0" type="primary" icon="el-icon-plus" circle style="margin-left: 5px"></el-button>-->
<!--              <el-button @click="optionMinus(index)"  v-if="index > 0" type="danger" icon="el-icon-minus" circle style="margin-left: 5px"></el-button>-->
            </el-col>
            <el-col :span="1"  v-for="(item,index) of optionArr" :key="index">
              <!--              <el-input v-model="optionArr[index].value" placeholder="请输入选项" style="width:90%;"></el-input>-->
<!--              <avue-ueditor v-model="optionArr[index].value" :options="ueditorOptions" style="width:90%;"></avue-ueditor>-->
              <el-button @click="optionPlus" v-if="index == 0" type="primary" icon="el-icon-plus" circle style="margin-left: 5px"></el-button>
              <el-button @click="optionMinus(index)"  v-if="index > 0" type="danger" icon="el-icon-minus" circle style="margin-left: 5px"></el-button>
            </el-col>
          </el-row>
        </div>
      </template>
      <template slot-scope="scope" slot="isAsrForm">
        <div>
          <!-- 单选题 -->
          <el-row v-if="form.qstType == 1">
            <el-col :span="24"  >
              <el-radio-group v-model="form.isAsr">
                <el-radio-button :label="indexToString(index)" v-for="(item,index) of optionArr" :key="index"></el-radio-button>
              </el-radio-group>
            </el-col>
          </el-row>
          <!-- 多选题 -->
          <el-row  v-if="form.qstType == 2">
            <el-col :span="24" >
              <el-checkbox-group v-model="checkedArr" >
                <el-checkbox-button :label="indexToString(index)" v-for="(item,index) of optionArr" :key="index"></el-checkbox-button>
              </el-checkbox-group>
            </el-col>
          </el-row>
          <!-- 判断题 -->
          <el-row v-if="form.qstType == 3">
            <el-col :span="24" >
              <el-radio-group v-model="form.isAsr">
                <el-radio-button label="A" >正确</el-radio-button>
                <el-radio-button label="B">错误</el-radio-button>
              </el-radio-group>
            </el-col>
          </el-row>
          <!-- 填空题 -->
          <el-row v-if="form.qstType == 4" v-for="(item,index) of optionArr" :key="index">
            <el-col :span="23"  >
             <!-- <div v-html="optionArr[index].value"  @click="openEditor(index)" v-if="!optionArr[index].editor" placeholder="请输入选项" style="line-height: 32px;border-radius: 4px;border: 1px solid #DCDFE6;min-height:32px;width:90%;"></div>-->
              <el-input v-model="optionArr[index].value"  v-if="!optionArr[index].editor" placeholder="请输入选项" style="width:90%;"></el-input>
              <!--<avue-ueditor v-model="optionArr[index].value" v-if="optionArr[index].editor" :options="ueditorOptions" style="width:90%;"></avue-ueditor>-->
<!--              <el-button @click="optionPlus" v-if="index == 0" type="primary" icon="el-icon-plus" circle style="margin-left: 5px"></el-button>-->
<!--              <el-button @click="optionMinus(index)"  v-if="index > 0" type="danger" icon="el-icon-minus" circle style="margin-left: 5px"></el-button>-->
            </el-col>
            <el-col :span="1"  >
              <!--              <el-input v-model="optionArr[index].value" placeholder="请输入选项" style="width:90%;"></el-input>-->
<!--              <avue-ueditor v-model="optionArr[index].value" :options="ueditorOptions" style="width:90%;"></avue-ueditor>-->
              <el-button @click="optionPlus" v-if="index == 0" type="primary" icon="el-icon-plus" circle style="margin-left: 5px"></el-button>
              <el-button @click="optionMinus(index)"  v-if="index > 0" type="danger" icon="el-icon-minus" circle style="margin-left: 5px"></el-button>
            </el-col>
          </el-row>
        </div>
      </template>
      <template slot-scope="scope" slot="qstTypeForm">
        <el-select v-model="form.qstType" placeholder="请选择" @change="changeQues">
          <el-option
              v-for="item in questionTypeList"
              :key="item.dictKey"
              :label="item.dictValue"
              :value="item.dictKey">
          </el-option>
        </el-select>
      </template>
    </avue-crud>
    <el-dialog title="试题数据导入"
               append-to-body
               :visible.sync="excelBox"
               width="555px">
      <avue-form :option="excelOption" v-model="excelForm" :upload-after="uploadAfter">
        <template slot="excelTemplate">
          <el-button type="primary" @click="handleTemplate()">
            点击下载<i class="el-icon-download el-icon--right"></i>
          </el-button>
        </template>
      </avue-form>
      <el-row v-if="errorMsg != ''" style="top:-45px;left:28px">
        <div>错误信息：</div>
        <el-col :span="2" ></el-col>
        <el-col :span="20" style="color: #f56c6c">
          <div style="margin:15px 0 0 -35px;padding-left: 19px;overflow: auto; height: 150px;width: 420px;border-radius:5px;border: #d9d9d9 1px dashed" v-html="errorMsg"></div>
        </el-col>
      </el-row>
    </el-dialog>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove,subjectExamTreeList,questionTypeList} from "@/api/exam/question";
  import {mapGetters} from "vuex";
  let _this;

  export default {
    data() {
      var isAsrValue = (rule, value, callback)=>{
        let checkeArrString='';
        if(_this.form.qstType==2){
          for (let i=0;i<_this.checkedArr.length;i++){
            if(_this.checkedArr[i]!=undefined&&_this.checkedArr[i]!=null&&_this.checkedArr[i]!=''){
             if(i==0){
               checkeArrString+=_this.checkedArr[i];
             }else {
               checkeArrString=checkeArrString+","+_this.checkedArr[i];
             }
            }
          }
          if(checkeArrString===''){
            callback(new Error("请选择答案"));
          }else {
            _this.form.isAsr=checkeArrString;
            callback();
          }
        }
        if(_this.form.qstType==4){
          for (let i=0;i<_this.optionArr.length;i++){
            if(_this.optionArr[i].value!=undefined&&_this.optionArr[i].value!=null&&_this.optionArr[i].value!=''){
              if(i==0){
                checkeArrString+=_this.optionArr[i].value;
              }else {
                checkeArrString=checkeArrString+","+_this.optionArr[i].value;
              }
            }
          }
          if(checkeArrString===''){
            callback(new Error("请选择答案"));
          }else {
            _this.form.isAsr=checkeArrString;
            callback();
          }
        }
        if(_this.form.qstType==1||_this.form.qstType==3){
          if (value === '') {
            callback(new Error("请选择答案"));
          } else {
            callback();
          }
        }
      };
      var optionListValue = (rule, value, callback)=>{
        let error='';
        let y=1;
        for (let i=0;i<_this.optionArr.length;i++){
          if(_this.optionArr[i].value==undefined||_this.optionArr[i].value==null||_this.optionArr[i].value===''){
            error+="第"+y+"行选项为空。";
          }else {
            _this.optionArr[i].editor=="false";
          }
          y+=1;
        }
        if (error === '') {
          console.log(JSON.stringify(_this.optionArr));
          _this.form.optionList=JSON.stringify(_this.optionArr);
          callback();
        } else {
          callback(new Error(error));
        }
      };
      return {
        qstContentFlag:false,
        qstAnalyzeFlag:false,
        excelBox: false,
        errorMsg:"",
        questionTypeList:[],
        ueditorOptions:{
          //普通图片上传
          action: "/api/common/upload?path=course",
          customConfig: {
          },
          props: {
            res: "data",
            url:'url'
          },
        },
        ueditorOptions2:{
          //普通图片上传
          action: "/api/common/upload?path=course",
          customConfig: {
          },
          props: {
            res: "data",
            url:'url'
          },
        },
        isAsr:"",
        optionArr: [{
          editor:false,
          value:""
        },{
          value:"",
          editor:false,
        }],
        search:{},
        checkedArr:[],
        form: {},
        query: {},
        loading: true,
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        selectionList: [],
        subjectExamTreeList:[],
        option: {
          searchShow: true,
          align: "center",
          tip: false,
          border: true,
          index: false,
          viewBtn: false,
          selection: true,
          columnBtn:false,
          labelWidth:130,
          column: [
            {
              label: "试题id",
              prop: "id",
              hide:false,
              addDisplay: false,
              editDisplay: false,
              search :true,
              rules: [{
                required: true,
                message: "请输入试题id",
                trigger: "blur"
              }]
            },
            {
              label: "专业分类",
              prop: "subjectId",
              type:"cascader",
              search:true,
              formslot:true,
              dicUrl: "/api/edu/subject/exam/tree",
              props: {
                label: "title"
              },
              tip:"试题专业分类只可以选择二级分类",
              rules: [{
                required: true,
                message: "请选择试题专业分类",
                trigger: "blur"
              }]
            },
            {
              label: "试题题干",
              prop: "qstContent",
              type:"ueditor",
              component: "avueUeditor",
              search :true,
              searchslot:true,
              formslot:true,
              options:{
                //普通图片上传
                action: "/api/common/upload?path=course",
                props: {
                  res: "data",
                  url:'url'
                },
              },
              rules: [{
                required: true,
                message: "请输入题干",
                trigger: "blur"
              }],
              span: 24,
            },
            {
              label: "试题类型",
              prop: "qstType",
              type: "select",
              dataType: "number",
              search :true,
              formslot:true,
              span: 24,
              value:1,
              props: {
                label: "dictValue",
                value: "dictKey"
              },
              dicUrl: "/api/crazy-system/dict/dictionary?code=exam_question_type",
              rules: [{
                required: true,
                message: "请选择试题类型",
                trigger: "blur"
              }]
            },
            {
              label: "填空题类型",
              prop: "vacancyType",
              addDisplay: false,
              editDisplay: false,
              hide: true,
              rules: [{
                required: true,
                message: "请输入填空题类型",
                trigger: "blur"
              }]
            },
            {
              label: "选项",
              prop: "optionList",
              hide: true,
              formslot:true,
              display: true,
              span:24,
              rules: [{ validator: optionListValue, trigger: 'blur' }]
            },
            {
              label: "正确答案",
              prop: "isAsr",
              formslot:true,
              span:24,
              rules: [{ validator: isAsrValue, trigger: 'blur' }]
            },
            {
              label: "试题难度",
              prop: "level",
              type: "select",
              dataType: "number",
              search :true,
              span: 24,
              value:1,
              props: {
                label: "dictValue",
                value: "dictKey"
              },
              dicUrl: "/api/crazy-system/dict/dictionary?code=exam_degree",
              rules: [{
                required: true,
                message: "请选择试题难度",
                trigger: "blur"
              }]
            },
            {
              label: "排序",
              prop: "sort",
              value:0,
              hide: true,
              addDisplay: false,
              editDisplay: false,
              rules: [{
                required: true,
                message: "请输入排序",
                trigger: "blur"
              }]
            },
            {
              label: "试题解析",
              prop: "qstAnalyze",
              hide: true,
              formslot:true,
              type:"ueditor",
              component: "avueUeditor",
              options:{
                //普通图片上传
                action: "/api/common/upload?path=course",
                props: {
                  res: "data",
                  url:'url'
                },
              },
              span: 24,
            },
            /*{
              label: "更新时间",
              prop: "updateTime",
              hide:true,
              addDisplay: false,
              editDisplay: false,
              rules: [{
                required: true,
                message: "请输入更新时间",
                trigger: "blur"
              }]
            },
            {
              label: "做过的次数",
              prop: "time",
              hide: true,
              addDisplay: false,
              editDisplay: false,
              rules: [{
                required: true,
                message: "请输入做过的次数",
                trigger: "blur"
              }]
            },
            {
              label: "该试题被做正确过多少道",
              prop: "rightTime",
              hide: true,
              addDisplay: false,
              editDisplay: false,
              rules: [{
                required: true,
                message: "请输入该试题被做正确过多少道",
                trigger: "blur"
              }]
            },
            {
              label: "该试题被做错过多少次",
              prop: "errorTime",
              hide: true,
              addDisplay: false,
              editDisplay: false,
              rules: [{
                required: true,
                message: "请输入该试题被做错过多少次",
                trigger: "blur"
              }]
            },
            {
              label: "正确率",
              prop: "accuracy",
              hide: true,
              addDisplay: false,
              editDisplay: false,
              rules: [{
                required: true,
                message: "请输入正确率",
                trigger: "blur"
              }]
            },*/
            {
              label: "创建时间",
              prop: "createTime",
              addDisplay: false,
              editDisplay: false,
              rules: [{
                required: true,
                message: "请输入添加时间",
                trigger: "blur"
              }]
            },
            /*{
              label: "试题作者",
              prop: "author",
              hide:true,
              addDisplay: false,
              editDisplay: false,
            },*/
          ]
        },
        data: [],
        excelForm: {},
        excelOption: {
          submitBtn: false,
          emptyBtn: false,
          column: [
            {
              label: '模板上传',
              prop: 'excelFile',
              type: 'upload',
              drag: true,
              loadText: '模板上传中，请稍等',
              span: 24,
              showFileList:false,
              propsHttp: {
                res: 'data'
              },
              tip: '请上传 .xls,.xlsx 标准格式文件',
              action: "/api/exam/question/import-exam"
            },
            {
              label: '模板下载',
              prop: 'excelTemplate',
              formslot: true,
              span: 24,
            }
          ]
        }
      };
    },
    computed: {
      ...mapGetters(["permission"]),
      permissionList() {
        return {
          addBtn: this.vaildData(this.permission.question_add, false),
          viewBtn: this.vaildData(this.permission.question_view, false),
          delBtn: this.vaildData(this.permission.question_delete, false),
          editBtn: this.vaildData(this.permission.question_edit, false)
        };
      },
      ids() {
        let ids = [];
        this.selectionList.forEach(ele => {
          ids.push(ele.id);
        });
        return ids.join(",");
      }
    },
    methods: {
      qstContentBlur(){
        this.qstContentFlag = true
      },
      handleImport() {
        this.excelBox = true;
      },
      handleTemplate() {
        window.open(`static/template/批量生成试题模版.xlsx`);
        // window.open(`/api/user/student/export-template?crazy-auth=${getToken()}`);
      },
      refreshChange() {
        this.onLoad(this.page, this.query);
      },
      uploadAfter(res, done, loading, column) {
        if(Object.prototype.toString.call(res)=="[object Error]"){
          this.errorMsg = res.message
          done();
          return;
        }
        done();
        this.errorMsg = ""
        this.excelBox = false;
        this.refreshChange();
      },
      qstContentFocus(){
        this.qstContentFlag = false
      },
      qstAnalyzeBlur(){
        this.qstAnalyzeFlag = true
      },
      qstAnalyzeFocus(){
        this.qstAnalyzeFlag = false
      },
      changeQues(value){
        console.log(value)
        if(value == "3"){
          _this.form.isAsr = "A"
        }
        if(value == "2"){
          _this.checkedArr = ["A"]
        }
        if(value == "4" || value == "3"){
          var optionList =_this.findObject(_this.option.column,'optionList')
          optionList.display = false
          _this.optionArr= [{
            value:"",
            editor:false,
          }]
        }else{
          _this.form.isAsr = "A"
          var optionList =_this.findObject(_this.option.column,'optionList')
          optionList.display = true
          _this.optionArr= [{
            value:"",
            editor:false,
          },{
            value:"",
            editor:false,
          },{
            value:"",
            editor:false,
          },{
            value:"",
            editor:false,
          }]
        }
      },
      openEditor(index){
        this.optionArr.forEach(function (e){
          e.editor = false
        })
        this.optionArr[index].editor = true
      },
      optionPlus(){
        this.optionArr.push({
          value:"",
          editor:false,
        })
      },
      optionMinus(index){
        this.optionArr.splice(index,1)
      },
      indexToString(index){
        var charcode;
        return index.toString(26).split("").map(function(item,i){
          charcode = item.charCodeAt(0);
          charcode+=(charcode>=97?10:49);
          return String.fromCharCode(charcode)
        }).join("").toUpperCase();
      },
      rowSave(row, done, loading) {
        add(row).then(() => {
          done();
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
        }, error => {
          window.console.log(error);
          loading();
        });
      },
      rowUpdate(row, index, done, loading) {
        update(row).then(() => {
          done();
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
        }, error => {
          window.console.log(error);
          loading();
        });
      },
      rowDel(row) {
        this.$confirm("确定将选择数据删除?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            return remove(row.id);
          })
          .then(() => {
            this.onLoad(this.page);
            this.$message({
              type: "success",
              message: "操作成功!"
            });
          });
      },
      handleDelete() {
        if (this.selectionList.length === 0) {
          this.$message.warning("请选择至少一条数据");
          return;
        }
        this.$confirm("确定将选择数据删除?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            return remove(this.ids);
          })
          .then(() => {
            this.onLoad(this.page);
            this.$message({
              type: "success",
              message: "操作成功!"
            });
            this.$refs.crud.toggleSelection();
          });
      },
      beforeOpen(done, type) {
        if (["edit", "view"].includes(type)) {
          getDetail(this.form.id).then(res => {
            this.form = res.data.data;
            if(res.data.data.qstType==1||res.data.data.qstType==2){
              _this.optionArr = JSON.parse(res.data.data.optionList);
            }
            if(res.data.data.qstType==2){
              _this.checkedArr = res.data.data.isAsr.split(",")
              // for(let i=0;i<res.data.data.isAsr.split(",").length;i++){
              //   _this.checkedArr = res.data.data.isAsr.split(",")[i]
              // }
            }
            if(res.data.data.qstType==4){
              console.log(res.data.data.isAsr.split(","));
              _this.optionArr = [];
              for(let i=0;i<res.data.data.isAsr.split(",").length;i++){
                _this.optionArr.push({
                  value:res.data.data.isAsr.split(",")[i],
                  editor:false,
                });
              }
            }
            let value = res.data.data.qstType
            if(value == "4" || value == "3"){
              var optionList =_this.findObject(_this.option.column,'optionList')
              optionList.display = false
            }else{
              var optionList =_this.findObject(_this.option.column,'optionList')
              optionList.display = true
            }
          });
        }
        if (["add", "view"].includes(type)) {
          for(let key in this.form){
            if(key=='qstType'){
              this.form[key]=1;
            }else if (key=='level'){
              this.form[key]=1;
            }else {
              this.form[key]=undefined;
            }
          }
          this.changeQues(1);
        };
        done();
      },
      searchReset() {
        this.query = {};
        this.page.currentPage = 1;
        this.onLoad(this.page);
      },
      searchChange(params,done) {
        this.query = params;
        this.page.currentPage = 1;
        this.onLoad(this.page, params);
        done()
      },
      selectionChange(list) {
        this.selectionList = list;
      },
      refreshChange() {
        this.onLoad(this.page, this.query);
      },
      selectionClear() {
        this.selectionList = [];
        this.$refs.crud.toggleSelection();
      },
      currentChange(currentPage){
        this.page.currentPage = currentPage;
      },
      sizeChange(pageSize){
        this.page.pageSize = pageSize;
      },
      onLoad(page, params = {}) {
        this.loading = true;
        if (typeof(this.query.subjectId) != "undefined"){
          this.query.subjectId = this.query.subjectId[1]
        }
        getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.data = data.records;
          this.loading = false;
          this.selectionClear();
        });
      }
    },
    created:function(){
      _this = this;
      subjectExamTreeList().then(res =>{
        this.subjectExamTreeList = res.data.data;
      })

      questionTypeList().then(res=>{
        let questionTypeList = res.data.data
        questionTypeList.forEach(e=>{
          e.dictKey = Number(e.dictKey)
        })
        this.questionTypeList = questionTypeList
      })
    },
  };
</script>

<style>
</style>
