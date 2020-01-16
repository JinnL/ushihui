package com.ekabao.oil.ui.activity;

import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ekabao.oil.R;

import butterknife.BindView;

public class ProtocolActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.title_lefttextview)
    TextView titleLefttextview;
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.title_centerimageview)
    ImageView titleCenterimageview;
    @BindView(R.id.title_righttextview)
    TextView titleRighttextview;
    @BindView(R.id.title_rightimageview)
    ImageView titleRightimageview;
    @BindView(R.id.view_line_bottom)
    View viewLineBottom;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_protocol)
    TextView tvProtocol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_protocol;
    }

    @Override
    protected void initParams() {
        titleCentertextview.setText("用户协议和隐私声明");
        titleLeftimageview.setOnClickListener(this);
        tvProtocol.setText(Html.fromHtml("<font color='#444444'>一、特别提示</font>" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在此特别提醒您（用户）在注册成为油实惠用户之前，请认真阅读《用户服务协议及隐私政策》（以下简称“协议”），确保您充分理解本协议中各项条款。油实惠由杭州乐言网络科技有限公司独立经营和管理。请您审慎阅读并选择接受或不接受本协议，您同意并点击确认本协议条款且完成注册程序后，才能成为油实惠的正式注册用户，并享受平台的各类服务。您的注册、登录、使用等行为将视为对本协议的接受，并同意接受本协议各项条款的约束。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;本协议是由用户与杭州乐言网络科技有限公司所签订的协议。“用户”是指注册、登录、使用本服务的个人、单位。本协议可由油实惠随时更新，更新后的协议条款一旦公布即代替原来的协议条款，恕不再另行通知，用户可在本APP中查阅最新版协议条款。在修改协议条款后，如果用户不接受修改后的条款，请立即停止使用油实惠提供的服务，用户继续使用油实惠提供的服务将被视为接受修改后的协议。" +
                "<br/><font color='#444444'>二、服务内容</font>" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2.1 油实惠基于互联网，包含油实惠网站、APP客户端等在内的各种形态（包括未来技术发展出现的新的服务形态）向您提供各项油卡充值、线上购物、客户服务等其他服务，具体服务内容由油实惠根据实际情况提供。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2.2 油实惠仅提供相关的网络服务，除此之外与相关网络服务有关的设备（如个人电脑、手机、及其他与接入互联网或移动网有关的装置）及所需的费用（如为接入互联网而支付的电话费及上网费、为使用移动网而支付的手机费）均应由用户自行负担。用户需理解并同意，使用本服务时会耗用终端设备和带宽等资源。" +
                "<br/><font color='#444444'>三、账号注册</font>" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.1 用户使用本服务前需要注册一个“油实惠”账号。“油实惠”账号应当使用手机号码绑定注册，请用户使用尚未与“油实惠”账号绑定的手机号码，以及未被油实惠根据本协议封禁的手机号码注册“油实惠”账号。油实惠可以根据用户需求或产品需要对账号注册和绑定的方式进行变更，而无须事先通知用户。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.2 用户一旦注册为“油实惠”用户，需对本人在此账户中所进行的所有活动负全权责任。若因个人原因导致其本人、油实惠平台或任何第三方造成损害，用户将承担全部责任。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.3 在用户注册及使用本服务时,油实惠需要搜集能识别用户身份的个人信息以便可以在必要时联系用户,为用户提供更好的使用体验。油实惠搜集的信息包括但不限于用户的姓名、身份证号、地址等信息;油实惠同意对这些信息的使用将受限于第五条用户隐私及保护的约束。" +
                "<br/><font color='#444444'>四、账户安全</font>" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4.1 用户一旦注册成功，成为“油实惠”的用户，将有权使用该账号密码登录油实惠平台进行使用，且可依照平台规定更改密码。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4.2 用户需妥善保管账户密码，不得向第三方透露。如因用户违反以上规定而造成第三方人员进行恶意刷单导致用户有重大经济损失，后果应用户自行承担。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4.3 由于账户关联用户的个人信息及油实惠平台商业信息，账户仅限用户本人使用。未经平台同意，用户直接或间接授权第三方使用该账户或获取账户信息的行为无效。若因此危及账户安全及平台信息安全，油实惠可拒绝提供相应服务或终止本协议。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4.4 若有发现用户冒用他人账号及密码，油实惠有权对其实际使用人进行连带追究。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4.5 若用户发现账号被盗、遭遇黑客行为或出现其他异常情况，或者本公司发现用户的账号存在被盗、共享或者被滥用等迹象或者其他异常情况，本公司将会对用户的账号进行调查，并根据情节，在调查期间对用户的账号采取保护性措施（包括但不限于暂时冻结账号）。请用户根据本公司的指引，提供相关信息以配合本公司的调查，以便尽快解除用户账号的保护性措施。" +
                "<br/><font color='#444444'>五、用户隐私及保护</font>" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;用户在同意本协议或使用油实惠任一服务之时，即视为用户已同意油实惠按照《用户服务协议及隐私政策》相关条款来合法使用和保护用户的个人信息。油实惠平台对于用户提供的、平台自行收集的、经认证的个人信息将按照本协议予以保护、使用或者披露。油实惠平台无需用户同意即可向平台关联实体转让与平台有关的全部或部分权利和义务。未经平台事先书面同意，用户不得转让其在本协议项下的任何权利和义务。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5.1 用户了解并同意，以下信息适用本隐私权政策：" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5.1.1 用户注册油实惠平台账户时，根据要求提供的个人注册信息；" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5.1.2 用户使用油实惠平台，或访问相关网页时，油实惠平台自动接收并记录的用户浏览器上的服务器数值，包括但不限于IP地址等数据及用户要求取用的网页记录；" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5.1.3 油实惠平台通过合法途径从商业伙伴处取得的用户个人数据。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5.2 用户了解并同意，以下信息不适用本隐私权政策：" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5.2.1 用户在使用油实惠平台提供的搜索服务时输入的关键字信息；" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5.2.2 油实惠平台收集到的用户在油实惠平台发布的有关信息数据（包括但不限于参与活动、成交信息及评价详情）；" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5.2.3 违反法律规定或违反油实惠平台规则行为及油实惠平台已对用户采取的措施。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5.3 为服务用户的目的，油实惠平台可能通过使用用户个人信息，向用户提供其感兴趣的信息，包括但不限于向用户发出产品和服务信息，或者与油实惠平台合作伙伴共享信息以便他们向用户发送有关其产品和服务的信息（后者需要用户事先同意）。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5.4 对于公开披露的用户的个人信息，平台会充分重视风险，在收到公开披露申请后第一时间且审慎审查其正当性、合理性、合法性，并在公开披露时和公开披露后采取不低于本《用户服务协议及隐私政策》约定的个人信息安全保护措施和手段的程度对其进行保护。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5.5 油实惠的账号具有安全保护功能，用户需对账户密码加以妥善保管，切勿将密码告知他人，因密码保管不善而造成的所有损失由用户自行承担。用户泄漏密码,有可能导致不利的法律后果，因此不管任何原因导致用户的密码安全受到威胁,用户应该立即和油实惠客服人员取得联系。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5.6 基于防火墙或其他安全软件可能发生故障，或按照当前商业上可以采取的安全手段也难以避免或及时消除的故障及破坏，将可能导致用户的信息遭到外部访问、窃取或删除，此等情形下油实惠平台不承担相应的责任。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5.7 因不可抗力所导致的用户资料及信息泄露（包含但不限于黑客攻击、第三方导致的系统缺陷等），油实惠平台不承担相应的责任。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5.8 如果油实惠发现或收到他人举报或投诉用户违反本协议约定的，平台有权视情节轻重对违规账号处以包括但不限于警告、账号封禁 、功能封禁的处罚，且通知用户处理结果。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5.9 用户理解并同意，油实惠有权合理判断对违反有关法律法规或本协议规定的行为进行处罚，对违法违规的任何用户采取适当的法律行动，并依据法律法规保存有关信息向有关部门报告等，用户应承担由此而产生的一切法律责任。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5.10 用户理解并同意，因用户违反本协议约定，导致或产生的任何第三方主张的任何索赔、要求或损失，包括合理的律师费，用户应当赔偿油实惠与合作公司、关联公司，并使之免受损害。" +
                "<br/><font color='#444444'>六、用户权利和义务</font>" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6.1 用户需在开始使用/注册程序即使用我方平台服务前，应当具备中华人民共和国法律规定的与行为相适应的民事行为能力。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6.2 用户应当严格遵守本协议及油实惠发布的其他协议、活动规则、站内公告等，因个人违反协议或规则的行为给第三方或油实惠造成损失的，用户应当承担全部责任。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6.3 用户有权选择是否成为油实惠平台用户。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6.4 用户有义务确保向油实惠平台提供的所有注册信息真实准确，包括但不限于姓名、身份证号、银行卡号、支付账号、联系电话、电子邮箱等，并保证油实惠平台可以通过上述联系方式及时与用户取得联系。同时，用户有义务在相关资料变更时及时更新注册信息。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6.5 用户不得利用油实惠平台网络服务系统进行任何不利于油实惠平台的行为；" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6.6 用户对登录后所持账号产生的行为依法享有权利且需对这些行为承担责任。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6.7 用户应当自行承担其所发布的信息内容所涉及的责任，不得利用油实惠平台提供的网络服务上传、展示或传播任何虚假的、骚扰性的、中伤他人的、辱骂性的、恐吓性的、庸俗淫秽的或其他任何非法的信息资料。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6.8 用户不得通过不正当的手段或其他不公平的手段使用本公司的软件和服务或参与本公司活动。用户不得干扰本公司正常地提供软件和服务，包括但不限于：攻击、侵入本公司的网站服务器或使用网站服务器过载；不合理地干扰或阻碍他人使用本公司所提供的软件和服务。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6.9 如果本公司发现用户数据异常或者存在违法或者其他不正当行为，本公司有权进行调查，并根据本协议规定，采取相应措施，且用户无权因此要求本公司承担任何责任。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6.10 油实惠有权通过APP或者用户所提供的联系方式发送给用户通告、活动通知及其他消息。" +
                "<br/><font color='#444444'>七、免责声明</font>" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;7.1 用户了解并同意，油实惠因下述任一情况而导致用户的任何损害及赔偿不承担任何责任，包括但不限于经济、商誉、使用、数据等方面的损失或其它无形损失的损害赔偿：" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;7.1.1 第三方未经批准地使用用户的账户或更改用户的数据；" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;7.1.2 通过油实惠平台服务购买或获取任何商品、样品、数据、信息等行为或替代行为产生的费用及损失；" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;7.1.3 用户对油实惠平台服务的误解；" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;7.1.4 任何非因油实惠的原因而引起的与平台服务有关的其它损失等。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;7.2 对于信息网络正常的设备维护，信息网络连接故障，电脑、通讯或其他系统的故障，电力故障，罢工，劳动争议，暴乱，起义，骚乱，生产力或生产资料不足，火灾，洪水，风暴，爆炸，战争，政府行为，司法行政机关的命令或第三方的不作为而造成的不能服务或延迟服务，油实惠不承担任何责任，但将尽力减少因此而给用户造成的损失和影响。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;7.3 用户明确同意其使用油实惠平台所存在的风险将完全由其自己承担，因其使用油实惠平台而产生的一切后果也由其自己承担。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;7.4 用户须了解并认可，任何通过本平台进行的交易并不能避免以下风险的产生，本平台不能也没有义务为宏观经济风险、政策风险、不可抗力因素导致的风险而对用户造成的损失负责，同时也包括因用户的过错导致的任何损失，该过错包括但不限于：操作不当、遗忘或泄露密码、密码被他人破解、用户使用的计算机系统被第三方侵入、用户委托他人代理交易时他人恶意或不当操作而造成的损失。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;7.5 油实惠平台不保证为向用户提供便利而设置的外部链接的准确性和完整性，同时，对于该等外部链接指向的不由油实惠平台实际控制的任何网页上的内容， 油实惠平台不承担任何责任。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;7.6 若油实惠平台已经明示其服务提供方式发生变更并提醒用户应当注意事项，用户未按要求操作所产生的一切后果由用户自行承担。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;7.7 与本公司合作的第三方机构向用户提供的服务由第三方机构自行负责，本公司不对此等服务承担任何责任。" +
                "<br/><font color='#444444'>八、知识产权条款</font>" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;8.1 油实惠在网络服务中提供的任何文本、图片、图形、音频和视频资料均受版权、商标权以及其他相关法律法规的保护。未经油实惠事先同意，任何人不能擅自复制、传播这些内容，或用于其他任何商业目的，所有这些资料或资料的任何部分仅可作为个人或非商业用途而保存在某台计算机内。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;8.2 油实惠为提供网络服务而使用的任何软件（包括但不限于软件中的任何文字、图形、音频、视频资料及其辅助资料）的一切权利属于该软件的著作权人，未经该著作权人同意，任何人不得对该软件进行反向工程、反向编译或反汇编。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;8.3 尊重知识产权是用户应尽的义务，如有违反，用户应对油实惠平台承担损害赔偿等法律责任。" +
                "<br/><font color='#444444'>九、服务终止条款</font>" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在下列情况下,油实惠有权终止向用户提供服务；" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;9.1 用户不得通过程序或人工方式进行刷量或作弊，若发现用户有作弊行为，油实惠将立即终止服务，并有权扣留账户内金额；" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;9.2 一旦油实惠发现用户提供的数据或信息中含有虚假内容，油实惠有权随时终止向该用户提供服务；" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;9.3 本服务条款终止或更新时，用户明示不愿接受新的服务条款；" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;9.4 在用户违反本服务协议相关规定时，油实惠有权终止向该用户提供服务；如该用户再一次直接或间接或以他人名义注册为用户的，一经发现，油实惠有权直接单方面终止向该用户提供服务；" +
                "<br/><font color='#444444'>十、法律管辖和适用</font>" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;10.1 本协议之订立、生效、解释、履行、修订与争议解决均适用中华人民共和国法律。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;10.2 因本协议引起的或与本协议有关的任何争议，各方应友好协商解决；协商不成的，任何一方均可将有关争议提交杭州仲裁委员会进行仲裁，仲裁裁决是终局的，对各方均有约束力。如果所涉及的争议不适于仲裁解决，应提交平台所在地有管辖权的人民法院诉讼解决。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;10.3 本协议是由用户与平台共同签订的，适用于用户在平台的全部活动。" +
                "<br/><font color='#444444'>十一、其他条款</font>" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;11.1 油实惠平台有权随时根据有关法律、法规的变化以及公司经营状况和经营策略的调整等修改本协议，而无需另行单独通知用户。修改后的协议会在油实惠平台公布。用户可随时通过油实惠微信/APP浏览最新服务协议条款。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;11.2 当发生有关争议时，以最新的协议文本为准。如果不同意油实惠平台对本协议相关条款所做的修改，用户有权停止使用网络服务。如果用户继续使用网络服务，则视为用户接受油实惠平台对本协议相关条款所做的修改。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;11.3 本协议的任何条款无论因何种原因无效或不具可执行性，其余条款仍有效，对双方具有约束力。" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;11.4 本协议最终解释权杭州乐言网络科技有限公司所有，并且保留一切解释和修改的权力。"+
                "<br/><font color='#444444'>十二、关于我们</font>" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;公司名称：杭州乐言网络科技有限公司" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;公司地址：浙江省杭州市江干区新塘路672号中豪国际商业中心4幢425室" +
                "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;联系电话：0571-87660338" +
                "<br/><br/>"
               ));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
        }
    }
}
