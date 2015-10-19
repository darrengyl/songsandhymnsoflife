
package com.church.psalm;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class ListsFragment extends Fragment{
	RecyclerView recyclerView;
	MaterialProgressBar progressBarList;
	EditText searchEditText;
	DBAdapter dbAdapter;
	ArrayList<Song> data;
	RecyclerViewAdapter adapter;
	boolean writeSongs = false;
	final static String prefsName = "com.church.psalm.DATABASE_PREFERENCE";
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View V = inflater.inflate(R.layout.fragment_lists, container, false);
		recyclerView = (RecyclerView)V.findViewById(R.id.recyclerview);
		recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()
				, DividerItemDecoration.VERTICAL_LIST));
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		progressBarList = (MaterialProgressBar)V.findViewById(R.id.progress_bar_list);
		dbAdapter = new DBAdapter(getContext());


		new readWriteAllSongs().execute();

		return V;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		inflater.inflate(R.menu.menu_list_fragment, menu);
		MenuItem searchMenu = menu.findItem(R.id.search);
		Drawable searchIcon = getResources().getDrawable(R.drawable.ic_search_black_24dp
				, getContext().getTheme());
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
			searchIcon.setTint(getResources().getColor(R.color.colorMenuIcon
					, getContext().getTheme()));
		} else {
			searchIcon.setTint(getResources().getColor(R.color.colorMenuIcon));
		}

		searchMenu.setIcon(searchIcon);
		//searchIcon.setColorFilter(
		//		getResources().getColor(R.color.colorAccent, getContext().getTheme())
		//		,);
		//searchEditText = (EditText)view.findViewById(R.id.search);
		//searchEditText.setText("");
		//searchEditText.setHint("Search Titles");


	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		/*if (item.getItemId() == R.id.search){
			if(searchEditText.requestFocus()){
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
			}
			return true;
		}*/
		return super.onOptionsItemSelected(item);
	}

	public void InsertSongs(){
		String[] title = {
				"荣耀荣耀归于父神"
				,"但愿荣耀归于圣父"
				,"赞美我神,祝福源头"
				,"荣耀,荣耀归于圣父"
				,"圣哉、圣哉、圣哉"
				,"全能君王降临"
				,"亲爱父神,我们敬拜"
				,"天上父神"
				,"喜乐,喜乐,我们敬拜"
				,"你的信实广大"
				,"我们从前所有"
				,"神的大爱"
				,"父神,我今敬拜你"
				,"荣耀归于父神"
				,"父啊,久在创世之前(一)"
				,"神你自古是我帮助"
				,"父啊久在创世之前(二)"
				,"荣耀父神赞美你"
				,"神啊你为何故"
				,"你爱所给虽然甚多"
				,"不能朽不能见独一全智神"
				,"神我赞美你"
				,"我们不会疲倦"
				,"哦神是你召我们"
				,"到父面前来"
				,"阿爸,我们来颂赞你"
				,"众子"
				,"父神我们称颂你"
				,"父啊我们在你面前拜礼"
				,"慈爱父神对于你的大爱"
				,"我父我神你爱在子显明"
				,"亲爱父神我们向你歌唱"
				,"父你爱子率领我们赞美"
				,"高举主名"
				,"耶稣大名荣耀有能"
				,"因耶稣的圣名人人当俯伏"
				,"耶稣这名超乎万名"
				,"我何等爱耶稣"
				,"主耶稣我爱你名"
				,"耶稣我爱这名"
				,"耶稣这名甜美芬芳"
				,"称颂主尊名"
				,"主我高举你尊名"
				,"耶稣这名何其甘甜"
				,"颂赞主圣名"
				,"这个真是何等甘美的故事"
				,"你的大爱过于人所能度"
				,"我的救主"
				,"细语"
				,"我有一位奇妙救主"
				,"谷中的百合花"
				,"永活的故事"
				,"听啊天使赞高声"
				,"请听佳音"
				,"哦来蒙恩群众"
				,"普世欢腾"
				,"主的路程"
				,"主你圣首满伤迹"
				,"哦主什么使你头垂"
				,"哎哟救主真曾流血"
				,"惊人话语"
				,"求主使我明看见"
				,"我救主爱无极"
				,"阿利路亚何等救主"
				,"主我宝贵你的行径"
				,"时已夜半晚星久沉"
				,"我每静念那十字架"
				,"祂从坟墓已起来"
				,"主已复活"
				,"救主基督已复活"
				,"祂仍然活着"
				,"在荣耀里有一人"
				,"加冠祂万有主"
				,"何等权能耶稣尊名"
				,"从前那戴荆棘的头"
				,"远超一切"
				,"冠祂万王之王"
				,"领我去髑髅地"
				,"这是谁在马槽中"
				,"荣耀归于你"
				,"听哪千万声音雷鸣"
				,"阿利路亚颂赞耶稣"
				,"看哪何等荣耀情景"
				,"我们歌唱要来掌权的王"
				,"乐哉因主为王"
				,"哦愿我有千万舌头"
				,"听那美妙天来歌声"
				,"蒙恩群众齐聚耶稣面前"
				,"同来朝见基督"
				,"你真伟大"
				,"赞美赞美"
				,"晨曦天际出现"
				,"赞美我主全能主神"
				,"主你真是配"
				,"荣耀归于我主"
				,"羔羊是配"
				,"赞美羔羊"
				,"凉爽你象柔和微风"
				,"你的美丽胜过曙光"
				,"我主耶稣惟你是配"
				,"最美的耶稣"
				,"庄严甘甜在主额上"
				,"哦主在你名里聚集"
				,"恩主你是生命源"
				,"玉漏沙残时将尽"
				,"哦主你是人心之乐"
				,"当世界还未曾创造"
				,"颂赞声音何等难得"
				,"我的诗歌是耶稣"
				,"主耶稣,当我们想到"
				,"耶稣只要一想到你"
				,"耶稣最爱救主"
				,"耶稣我主"
				,"何等奇妙救主"
				,"神的羔羊"
				,"惟有耶稣"
				,"千万感谢归耶稣"
				,"我听慈爱的话"
				,"永远举起耶稣"
				,"今主耶稣满足我心"
				,"我神何伟大"
				,"敬拜主要趁现在"
				,"敬拜我王"
				,"胜过一切"
				,"归于你圣名"
				,"为着这饼和这杯"
				,"耶稣基督我们记念你"
				,"我们照你恩惠话语"
				,"我们聚集围你桌前"
				,"耶稣,在你宝贝名里"
				,"我主耶稣,在你桌前"
				,"在此我要与你面对面"
				,"神圣、爱的珍馐"
				,"主举爱旗遮盖我们"
				,"奇妙十字架"
				,"十架大能"
				,"求主使我近十架"
				,"荣耀归主名"
				,"宝血已将我罪洗净"
				,"我主耶稣是我的义"
				,"惟有你宝血"
				,"救主流血所成救恩"
				,"奇妙"
				,"我今对你怜悯负债"
				,"基督盘石"
				,"起来我魂"
				,"自从我被赎回"
				,"我要歌颂我的救主"
				,"得赎"
				,"有福的确据"
				,"自基督来住在我心"
				,"荣耀的释放"
				,"在加略山"
				,"我是个罪人蒙主恩"
				,"惊人恩典"
				,"天来恩典漫我罪过"
				,"远在高天神宝座前"
				,"亲爱主你属我"
				,"完全救恩"
				,"我的信心安息之地"
				,"我今饮于永不干涸的活泉"
				,"来啊你这万福泉源"
				,"满　足"
				,"你里完全"
				,"惟知道我所信的"
				,"站在应许上"
				,"今有荣光照耀我魂间"
				,"基督使我心中喜乐"
				,"主活我里面"
				,"完全释放"
				,"爱何大寻回我"
				,"坦然无惧来到神前"
				,"再次"
				,"生命活水的江河"
				,"主流过我"
				,"哦神向我吹气"
				,"生命之气"
				,"生命之主向我吹气"
				,"主求你向我吹圣灵"
				,"求圣灵向我吹风"
				,"充满我"
				,"浇灌我"
				,"祝福赐下有如甘霖"
				,"充满"
				,"主在你灵里"
				,"我愿作主活水运河"
				,"主啊每逢你民同聚"
				,"圣徒众心爱里相系"
				,"同来赞美我们救主"
				,"福哉以爱联系"
				,"耶稣聚集我们在一起"
				,"愿神同在"
				,"爱主之人所显景象"
				,"看哪,何等美善!"
				,"我寻得赐生命者"
				,"成为圣别"
				,"教会惟一的根基"
				,"神和人都一无所缺"
				,"主我爱你国度"
				,"这里的爱"
				,"我是属祂祂属我"
				,"来与我同欢唱"
				,"耶稣竟然爱我"
				,"我有一友"
				,"我在拿撒勒人耶稣面前"
				,"深哉深哉耶稣的爱"
				,"爱的音乐"
				,"轻诉"
				,"活着在于你神圣的爱"
				,"惟一的至宝耶稣"
				,"让我爱你"
				,"恩主我爱你"
				,"我爱我主"
				,"至美的享受"
				,"爱的倾诉"
				,"人心所爱若非耶稣"
				,"在主耶稣心头"
				,"我在这里敬拜"
				,"谁能象我耶稣"
				,"哦主求你长在我心"
				,"我今面向高处直登"
				,"求主带我上到高山"
				,"我心所追求乃是神自己"
				,"哦主带着圣别之火"
				,"进来我主"
				,"光中之光照进"
				,"使我顺服"
				,"我每时刻需你"
				,"我真需要耶稣"
				,"你爱丰满洋溢"
				,"神圣之爱远超众爱"
				,"你是我的生命"
				,"靠近主"
				,"愿我爱你更深"
				,"毫无间隔"
				,"我魂中的吸引"
				,"没有间隔主"
				,"天程旅客的粮食"
				,"更多认识基督"
				,"主啊照你旨意"
				,"主我是属你"
				,"愿你为大"
				,"主你作我的领港"
				,"紧握我手"
				,"耶和华求你带领我"
				,"如鹿切慕溪水"
				,"主我心属你"
				,"爱的经历"
				,"与我同住"
				,"我属于主"
				,"主耶稣我曾应许"
				,"我今属于耶稣"
				,"为你我舍生命"
				,"主你得着我一生"
				,"主你得胜"
				,"你这不肯放我之爱"
				,"何等惭愧何等痛悔"
				,"你灵岂非已见祂过"
				,"远远丢背后"
				,"绝对分别出来"
				,"我献一切于祭坛"
				,"一切全奉献"
				,"都归基督"
				,"照你企图主"
				,"一切献于祭坛"
				,"为我"
				,"我不属自己"
				,"我举起心向你"
				,"我今撇下一切事物"
				,"活着为耶稣"
				,"跟随"
				,"我的荣耀得胜君王"
				,"我爱我爱我恩主"
				,"我当如何爱我的主"
				,"我是否要背负十架"
				,"在主这边"
				,"我是你的一生"
				,"献上以撒"
				,"我宁愿有耶稣"
				,"当人弃绝地的贿赂"
				,"我是一只笼中小鸟"
				,"我曾听见耶稣说道"
				,"稳固的根基"
				,"哦我魂可无恐"
				,"安息你这疲心"
				,"我以信心仰望"
				,"转眼仰望主耶稣"
				,"应当一无挂虑"
				,"何时需要信靠"
				,"哦前来投靠耶稣"
				,"我要如何赞美恩主"
				,"宝贝名"
				,"神啊你名何等广大泱漭"
				,"安稳在基督手臂"
				,"神必定顾念你"
				,"主顾念吗"
				,"亲爱主握我手"
				,"我主必供应"
				,"数主祝福"
				,"每一天"
				,"让我们在主里面常喜乐"
				,"神却曾应许"
				,"主赐更多恩典"
				,"迫得太紧"
				,"既有耶稣在我身旁"
				,"祂的脸面祂的天使常看见"
				,"我心安静"
				,"主啊我的人生路途"
				,"十字架的喜乐"
				,"主的完全平安"
				,"在主十架之下"
				,"我今在地的喜乐"
				,"从伯利恒我们动身"
				,"破毁带我到主怀"
				,"你怎无伤痕"
				,"后是膏油先是宝血"
				,"由死而生"
				,"估量生命原则"
				,"你若不压橄榄成渣"
				,"十字架的道路"
				,"如果我的道路"
				,"众人涌进主的国度"
				,"合为一"
				,"与你合一,永远之子"
				,"我曾经是满了罪污"
				,"一直走十架窄路"
				,"与祂一样"
				,"我与基督已同钉死"
				,"基督曾在加略受死"
				,"时时刻刻"
				,"求主宝血洁净我"
				,"比雪更白"
				,"非我惟主"
				,"圣灵发出祂光芒"
				,"主这灵神圣真理"
				,"主啊你美丽自己是我所慕"
				,"生命的赞美"
				,"世上景色我已一阅"
				,"指示你路"
				,"哦神鉴察我心我行"
				,"主啊光照"
				,"求主把我捆绑"
				,"卑微再卑微"
				,"勿将我废弃"
				,"你这神的隐藏的爱"
				,"主啊怜悯"
				,"神的大爱"
				,"基督虽能千回"
				,"哦主耶稣当你在地"
				,"祂为我死"
				,"十架越重神越亲近"
				,"要思想耶稣"
				,"你们能否顺从"
				,"永久盘石"
				,"当向标竿力前"
				,"前去我们口号"
				,"耶稣为我"
				,"基督就是我的世界"
				,"我已来到这生命之泉"
				,"主你已使你的自己"
				,"你是平静秘密之源"
				,"总是充足而有余"
				,"我本眼瞎"
				,"我神我爱我的永分"
				,"因祂活着"
				,"父神所赐慈爱何大"
				,"我的牧者是耶和华"
				,"在基督里"
				,"祂是一切最亲"
				,"基督属我"
				,"向主欢呼"
				,"我主耶稣是生命源"
				,"欢唱神圣的奥秘"
				,"神人"
				,"父我知道我的一生"
				,"主你荣面是我所羡"
				,"愿主为我擘开生命的"
				,"启示更多亮光"
				,"安静之时"
				,"真神道成肉身"
				,"请说主"
				,"主你没有一句为我"
				,"古老话语"
				,"我心宝爱主的圣言"
				,"神的话如丰富宝库"
				,"要对这山说去"
				,"复兴你工作主"
				,"祷告之时甘甜之时"
				,"再唱信心的歌"
				,"我必须告诉主"
				,"我奉耶稣全能的名"
				,"何等朋友我主耶稣"
				,"主让我心仍转向你"
				,"用功追求圣洁"
				,"与你更亲我神"
				,"在祂同在的秘密处"
				,"在花园中"
				,"与主开始一天"
				,"哦,你这不知名行者"
				,"我已相信真事实"
				,"恩主我今凭信摸你"
				,"耶稣我今欢然安息"
				,"住在你里面"
				,"安息在那永久膀臂中"
				,"我今住在主里面"
				,"专心仰望耶稣"
				,"信靠耶稣何其甘甜"
				,"信靠耶稣"
				,"主你的应许我全都接受"
				,"哦在荣耀里的基督"
				,"信而顺从"
				,"进入幔内"
				,"意志薄弱能力软弱"
				,"她已摸着祂的衣穗"
				,"祂带领我"
				,"古老的十字架"
				,"前途如何我不知"
				,"古圣信仰仍然活着"
				,"一路我蒙救主引领"
				,"求主不要向我掩面"
				,"从我活出你的自己"
				,"主使我更爱你"
				,"哦我要象你"
				,"让我爱"
				,"响起福音号声"
				,"往普天下去"
				,"速兴起传福音"
				,"信徒赶快遵命出去"
				,"愿我成祝福"
				,"爱主要我何去我就去"
				,"我岂可去双手空空"
				,"主救人"
				,"何等奇妙的救主"
				,"来尝主恩"
				,"我今为你祈求"
				,"我真不知"
				,"一日"
				,"绝没有"
				,"罪人之友是我耶稣"
				,"昨日今日直到永远"
				,"祂去加略"
				,"耶稣救主神儿子"
				,"主替我还尽"
				,"真福音"
				,"在十架"
				,"我一见这血"
				,"耶稣才能救我"
				,"我重担已卸"
				,"我为什么忧惧疑惑"
				,"无他惟有耶稣宝血"
				,"耶稣宝血,珍贵无比"
				,"今有一泉血流盈满"
				,"曾否在羔羊血洗清洁"
				,"洗罪泉源"
				,"权能是在血"
				,"无爱可与相比"
				,"耶稣我魂的爱人"
				,"这世界的光是耶稣"
				,"我需你亲爱耶稣"
				,"生命永远生命"
				,"一再尝试终归徒然"
				,"何等柔细慈爱,耶稣在呼召"
				,"谁能免我死亡凶路"
				,"耶稣今召你"
				,"主我要回家"
				,"望就活"
				,"飘渺人生何短暂"
				,"莫把我漏掉"
				,"进入我心"
				,"我爱述说这故事"
				,"主断开一切锁链"
				,"爱救了我"
				,"请对我讲主福音"
				,"耶稣来此"
				,"你们必须重生"
				,"求主满我杯"
				,"九十九只"
				,"照我本相无善足称"
				,"耶稣我来"
				,"你的欢迎声音"
				,"各样都齐备"
				,"奇妙的恩典"
				,"亲爱罪人请你来"
				,"灵中干渴的人快来"
				,"若尽得世界"
				,"与主同葬"
				,"来吧圣灵犹如良鸽"
				,"殷勤作工费心费力"
				,"你是人的真光"
				,"我们有位荣耀君王"
				,"求主容我与你同行"
				,"向我说话"
				,"哦主天天呼召"
				,"前进基督精兵"
				,"惟有常出代价"
				,"我神是我大能堡垒"
				,"基督精兵兴起"
				,"兴起兴起为基督"
				,"当奉耶稣这名站住"
				,"已经得胜且胜而又胜"
				,"我对撒但总是说不"
				,"安息于你"
				,"神的儿子出战前方"
				,"美好的仗"
				,"我不敢稍微失败"
				,"阿利路亚耶稣得胜"
				,"你要不死你要被提"
				,"当我恩主降临时候"
				,"自伯大尼"
				,"救我恩典"
				,"面对面见我的救主"
				,"那日的盼望"
				,"看清晨已经要破晓"
				,"我王必定快要再临"
				,"教会长久等待"
				,"我们神的荣美圣城"
				,"圣城同救主是我们目标"
				,"颂赞与尊贵与荣耀归你"
				,"祂的名是奇妙"
				,"快乐日"
				,"我心中奏一甜美音乐"
				,"感谢主"
				,"祂已被高举"
				,"耶和华所救赎的民"
				,"看哪神的帐幕在人间"
				,"在主里常常喜乐"
				,"何等的慈爱"
				,"看哪这是我主"
				,"耶稣我主荣耀王"
				,"教会的赞美"
				,"思念"
				,"产业"
				,"难以分离"
				,"遇见你们"
				,"我们呼吸天上空气"
				,"归宿"
				,"过教会生活"
				,"过节"
				,"我们一生一定永定"
				,"我们是一家人"
				,"花费"
				,"我有何代价不能付"
				,"爱使我们回家"
				,"我们是一"
				,"爱的踪迹"
				,"是爱的神作我牧人"
				,"主爱长阔高深"
				,"爱歌"
				,"无限无价的你"
				,"羡慕活在你面前"
				,"我要向高山举目"
				,"我今俯伏主面前"
				,"亲爱主宝贝主"
				,"主你所有一切工作"
				,"一生聪明未遇敌手"
				,"神你正在重排我的前途"
				,"求主启示主自己"
				,"在你里满安息"
				,"有时偶是青天"
				,"与主真是一"
				,"求主把我捡起"
				,"在信里的音韵"
				,"祂是主"
				,"我躺卧在溪水旁"
				,"与主同坐天上"
				,"我因主流泪"
				,"我已拣选主耶稣"
				,"我若稍微偏离正路"
				,"显现"
				,"漂泊"
				,"归入甜美的你"
				,"需要"
				,"真爱"
				,"芦苇之歌"
				,"怜悯慈爱宽恕"
				,"清晨的日光"
				,"我已得到真正自由"
				,"更美的家乡"
				,"陪我生活"
				,"信的故事"
				,"听我唱奇妙爱"
				,"受浸的见证"
				,"变化"
				,"我能否忘快来的主"
				,"哦主撒冷是你所建设"
				,"当我跑尽应跑道路"

		};
		for (int i = 0; i < title.length; i++){
			if (dbAdapter.insertSongData(i+1, title[i], 0, 0 , ""
					, 0) == -1){
				Log.d("Database error", "error occurred when inserting" + String.valueOf(i+1) + title[i]);
			}
		}

	}

	public void setDatabaseSharedPreference(){
		SharedPreferences sharedPreferences = getActivity().getSharedPreferences(prefsName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.putBoolean("databaseLoaded", true);
		editor.commit();
	}

	public boolean existDatabaseSharedPreference(){
		SharedPreferences sharedPreferences = getActivity().getSharedPreferences(prefsName, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean("databaseLoaded", false);
	}

	public class readWriteAllSongs extends AsyncTask<Void, ArrayList<Song>, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBarList.setVisibility(View.VISIBLE);
			recyclerView.setVisibility(View.GONE);

		}

		@Override
		protected Void doInBackground(Void... params) {
			if (existDatabaseSharedPreference()){
				Log.d("ListFragment Database", "Database is found");
			} else {
				Log.d("ListFragment Database", "Database can't be found");
				InsertSongs();
				setDatabaseSharedPreference();

			}
			data = dbAdapter.getAllSongs();

			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);

			progressBarList.setVisibility(View.GONE);
			recyclerView.setVisibility(View.VISIBLE);

			adapter = new RecyclerViewAdapter(getContext(),data);
			adapter.notifyDataSetChanged();
			recyclerView.setAdapter(adapter);





		}

	}
}

