package com.getqiu.event.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;
import com.huaban.analysis.jieba.SegToken;

public class CharactorSegmentor {
	
	private  static JiebaSegmenter segmenter = new JiebaSegmenter();
	
	/**
	 * 分词，并去除重复的词
	 * */
	public static Set<String> seperate(String sentence)
	{
		String cleanedSentence = sentence.replaceAll("[\\pP‘’“”\\s|0-9\\+\\-]", "");
		
		Set<String> wordsSet = new HashSet<>();
		List<SegToken> wordsList = segmenter.process(cleanedSentence, SegMode.SEARCH);
		for(SegToken s:wordsList){

			wordsSet.add(s.word);
		}
		
		return wordsSet;
		
	}
}
