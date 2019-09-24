VueSetup({
    template:
        `<div>
        写出英文<br/>
      <a>{{word.chinese}}</a>
      <el-input v-model="input"></el-input>
      <el-button @click="click">确定</el-button>
    </div>`,
    props: ["word"],
    data() {
        return {
            input: undefined
        };
    },
    methods: {
        click() {
            this.$emit("result", this.input === this.word.english);
        }
    }
})