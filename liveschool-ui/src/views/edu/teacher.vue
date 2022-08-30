<template>
  <basic-container>
    <avue-crud :option="option"
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
               @on-load="onLoad">
      <template slot="menuLeft">
        <el-button type="primary"
                   size="small"
                   @click="handleDelete">确定
        </el-button>
      </template>
    </avue-crud>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove} from "@/api/user/teacher";
  import {mapGetters} from "vuex";

  export default {
    data() {
      return {
        form: {},
        query: {},
        loading: true,
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        selectionList: [],
        option: {
          tip: false,
          border: true,
          align: "center",
          index: false,
          refreshBtn:false,
          viewBtn: true,
          addBtn: false,
          menu: false,
          selection: true,
          column: [
            {
              label: "教师名称",
              prop: "name",
              search:true,
              span: 24,
              rules: [{
                required: true,
                message: "请输入教师名称",
                trigger: "blur"
              }]
            },
            {
              label: "教师资历",
              prop: "education",
              type: "textarea",
              span: 24,
              rules: [{
                required: true,
                message: "请输入教师资历",
                trigger: "blur"
              }]
            },

            {
              label: "教师头衔",
              prop: "teaTitle",
              type: "select",
              dataType: "number",
              span: 24,
              props: {
                label: "dictValue",
                value: "dictKey"
              },
              dicUrl: "/api/crazy-system/dict/dictionary?code=tea_title",
              rules: [{
                required: true,
                message: "请输入教师头衔",
                trigger: "blur"
              }]
            },
            {
              label: "头像",
              prop: "headImg",
              type:"upload",
              dataType: 'string',
              span: 24,
              action: '/api/common/upload?path=teacher',
              propsHttp: {
                res: 'data'
              },
              listType: 'picture-img',
              rules: [{
                required: true,
                message: "请输入头像",
                trigger: "blur"
              }]
            },
            {
              label: "来源",
              prop: "type",
              type: "radio",
              span: 24,
              value: "1",
              dicData:[
                {
                  label:'网校',
                  value:"1"
                },{
                  label:'线下课',
                  value:"2"
                }
              ],
            },
            {
              label: "讲师专业",
              prop: "subjectId",
              search:true,
              type: "select",
              span: 24,
              props: {
                label: "subjectName",
                value: "id"
              },
              dicUrl: "/api//edu/subject/subjectList?parentId=0&type=course",
              rules: [{
                required: true,
                message: "请输入专业id",
                trigger: "blur"
              }]
            },
            {
              label: "排序",
              prop: "sort",
              type: "number",
              value: 0,
            },
            {
              label: "教师简介",
              prop: "profile",
              type:"ueditor",
              component: "avueUeditor",
              options:{
                //普通图片上传
                action: "/api/common/upload?path=teacher",
                props: {
                  res: "data",
                  url:'url'
                },
              },
              span: 24,
              rules: [{
                required: true,
                message: "请输入教师简介",
                trigger: "blur"
              }]
            },
            {
              label: "创建时间",
              prop: "createTime",
              editDisplay: false,
              addDisplay: false,
              rules: [{
                required: true,
                message: "请输入创建时间",
                trigger: "blur"
              }]
            },
          ]
        },
        data: []
      };
    },
    computed: {
      ...mapGetters(["permission"]),
      permissionList() {
        return {
          addBtn: this.vaildData(this.permission.teacher_add, false),
          viewBtn: this.vaildData(this.permission.teacher_view, false),
          delBtn: this.vaildData(this.permission.teacher_delete, false),
          editBtn: this.vaildData(this.permission.teacher_edit, false)
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
      rowSave(row, loading, done) {
        add(row).then(() => {
          loading();
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
        }, error => {
          done();
          console.log(error);
        });
      },
      rowUpdate(row, index, loading, done) {
        update(row).then(() => {
          loading();
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
        }, error => {
          done();
          console.log(error);
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
        if (this.selectionList.length > 1) {
          this.$message.warning("只能选择一条数据");
          return;
        }
        this.$emit('datasubmit',this.selectionList[0])
      },
      beforeOpen(done, type) {
        if (["edit", "view"].includes(type)) {
          getDetail(this.form.id).then(res => {
            this.form = res.data.data;
          });
        }
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
        getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.data = data.records;
          this.loading = false;
          this.selectionClear();
        });
      }
    }
  };
</script>

<style>
</style>
