package kr.co.tworld.shop.my.common.block;

import kr.co.tworld.shop.my.common.block.cache.CacheConstants;
import kr.co.tworld.shop.my.common.block.cache.CacheUtils;
import kr.co.tworld.shop.my.common.block.exceptions.BizException;
import kr.co.tworld.shop.my.common.block.exceptions.error.CommonError;
import kr.co.tworld.shop.my.common.block.model.UrlBlockMeta;
import kr.co.tworld.shop.my.common.block.model.UrlBlockPassId;
import kr.co.tworld.shop.my.common.block.model.UrlBlockPassKey;
import kr.co.tworld.shop.my.common.block.session.SessionUtil;
import kr.co.tworld.shop.my.common.block.session.UserSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Slf4j
public class BlockUrlComponent {

	@Autowired
	private BlockUrlRedisDao blockUrlRedisDao;

	private static final String FIELD_ID_URL_BLOCK_CODE     = "URLBlockCode";	    // URL 차단코드 필드 ID
	private static final String FIELD_ID_BLOCK_INFO         = "BlockInfo";	        // 차단정보 필드 ID
	private static final String META_SUBKEY_BLOCK_M_BLCODE  = ":block.M.blCode";	// BFF 모바일 차단코드 서브메타키
	private static final String META_SUBKEY_BLOCK_PASS_ID   = ":block.passId";      // 우회 ID 서브메타키
	private static final String META_SUBKEY_BLOCK_PASS_KEY   = ":block.passKey";      // 우회 Key 서브메타키

	/**
	 * <ul>
	 * <li>업무명 : URL 차단체크</li>
	 * <li>설 명 : 관리자가 등록한 차단정보에 해당되는지 확인한다.</li>
	 * <li>작성일 : 2018. 9. 19.</li>
	 * <li>작성자 : P128161</li>
	 * <li>
	 * @param url
	 * @param parameterMap
	 * </li>
	 * <li>
	 * @throws BizException
	 * </li>
	 * </ul>
	 */
	public String check(String blockHiddenKey, HttpSession session, String url, Map<String,String[]> parameterMap) throws BizException {
		String guideUrl = null; // 차단안내페이지

		// 우회 ID 체크
		if (isPassId(session)) {
			return null;
		}

		// 우회 KEY 체크
		if (isPassHiddenKey(blockHiddenKey)) {
			return null;
		}

		// 차단 URL 체크
		List<String> urlBlockCodeList = blockUrlRedisDao.getUrlBlockCodeList();
		if (urlBlockCodeList != null) {
			for (String blockCode: urlBlockCodeList) {
				UrlBlockMeta urlBlockMeta = blockUrlRedisDao.getUrlBlockMeta(blockCode);
				if (Objects.nonNull(urlBlockMeta)) {
					guideUrl = checkUrl(url, urlBlockMeta, parameterMap);
					if (StringUtils.isNotEmpty(guideUrl)) {
						return guideUrl;
					}
				}
			}
		}

		return null;
	}

	/**
	 * <ul>
	 * <li>업무명 : 우회 ID 확인</li>
	 * <li>설 명 : UrlBlockMeta 등록된 URL 정보의 차단여부를 확인한다.</li>
	 * <li>작성일 : 2018. 12. 14.</li>
	 * <li>작성자 : P128161</li>
	 * <li>
	 * </li>
	 * <li>
	 * @throws BizException
	 * </li>
	 * </ul>
	 */
	private boolean isPassId(HttpSession session) {
		UrlBlockPassId urlBlockPassId = blockUrlRedisDao.getUrlBlockPassId();
		if (Objects.nonNull(urlBlockPassId)) {

			String passIds = urlBlockPassId.getBlUrl();
			if (StringUtils.isNotEmpty(passIds)) {

				List passIdList = Arrays.asList(passIds.split(";"));
				if (! CollectionUtils.isEmpty(passIdList)) {

					if (Objects.nonNull(session)) {
						UserSession userSession = SessionUtil.getUser(session);

						if (Objects.nonNull(userSession)) {
							return passIdList.stream().anyMatch(passId -> passId.equals(userSession.getLoginId()));
						}
					}
				}
			}
		}

		return false;
	}

	/**
	 * <ul>
	 * <li>업무명 : 우회 (Cookie Key) 확인</li>
	 * <li>설 명 : UrlBlockMeta 등록된 Hidden Key 정보를 확인한다.</li>
	 * <li>작성일 : 2019. 07. 19.</li>
	 * <li>작성자 : P028874</li>
	 * <li>
	 * </li>
	 * <li>
	 * @throws BizException
	 * </li>
	 * </ul>
	 */
	private boolean isPassHiddenKey(String blockHiddenKey) {

		UrlBlockPassKey urlBlockPassKey = blockUrlRedisDao.getUrlBlockPassKey();
		if (Objects.nonNull(urlBlockPassKey)) {

			String passKeys = urlBlockPassKey.getBlUrl();
			if (StringUtils.isNotEmpty(passKeys)) {

				List passKeyList = Arrays.asList(passKeys.split(";"));
				if (! CollectionUtils.isEmpty(passKeyList)) {

					if (Objects.nonNull(blockHiddenKey)) {

						return passKeyList.stream().anyMatch(passKey -> passKey.equals(blockHiddenKey));
					}
				}
			}
		}

		return false;
	}

	/**
	 * <ul>
	 * <li>업무명 : Check Url</li>
	 * <li>설 명 : UrlBlockMeta 등록된 URL 정보의 차단여부를 확인한다.</li>
	 * <li>작성일 : 2018. 9. 19.</li>
	 * <li>작성자 : P127602</li>
	 * <li>
	 * @param url
	 * @param urlBlockMeta
	 * </li>
	 * <li>
	 * @throws BizException
	 * </li>
	 * </ul>
	 */
	private String checkUrl(String url, UrlBlockMeta urlBlockMeta, Map<String,String[]> parameterMap) {
		return checkUrlBlockMeta(url, urlBlockMeta, urlBlockMeta.getBlStDtm(), urlBlockMeta.getBlEdDtm(), parameterMap);
	}

	/**
	 * <ul>
	 * <li>업무명 : Check UrlBlockMeta</li>
	 * <li>설 명 : UrlBlockMeta 등록된 차단정보의 차단여부를 확인한다.</li>
	 * <li>작성일 : 2018. 9. 19.</li>
	 * <li>작성자 : P127602</li>
	 * <li>
	 * @param url
	 * @param urlBlockMeta
	 * @param startDtm
	 * @param endDtm
	 * </li>
	 * <li>
	 * @throws BizException
	 * </li>
	 * </ul>
	 */
	private String checkUrlBlockMeta(String url, UrlBlockMeta urlBlockMeta, String startDtm, String endDtm, Map<String,String[]> parameterMap) {
		String blockedUrl = urlBlockMeta.getBlUrl();
		if (StringUtils.isNotEmpty(startDtm) && StringUtils.isNotEmpty(endDtm)) {
			long nowDate = Long.parseLong(getCurrentTime());
			long startBlockTime = Long.parseLong(startDtm);
			long endBlockTime = Long.parseLong(endDtm);
			boolean checkTime = (startBlockTime <= nowDate && endBlockTime > nowDate);

			boolean checkUrl;
			if ("GR".equals(urlBlockMeta.getBlType())) {  // 그룹 URL차단
				checkUrl = Arrays.asList(blockedUrl.split(";")).stream().anyMatch(blockUrl -> blockUrl.equals(url));
			} else {
				checkUrl = url.equals(blockedUrl) ;
			}

			if( "*".equals(blockedUrl) && !"/error".equals(url) && !"/block/hidden-key".equals(url)) {
				checkUrl = true;
			}

			boolean isBlock = checkUrl && checkTime;
			if (isBlock) {
				if ("Y".equals(urlBlockMeta.getParamYn())) { // 파라미터 차단여부
					if (!isBlockedParameterValue(urlBlockMeta.getParamName(), urlBlockMeta.getParamValue(), parameterMap)) {
						return null;
					}
				}
				// 예외처리 URL 여부
				if("GR".equals(urlBlockMeta.getBlType()) && urlBlockMeta.getNblUrl() != null){
					log.debug("request URL : {}", url);
					// 예외처리 URL 체크.
					if(Arrays.asList(urlBlockMeta.getNblUrl().split(";")).stream().anyMatch(nblockUrl -> nblockUrl.equals(url))) {
						return null;
					}
					// 예외처리 URL 패턴 체크 ex) /dsds/**
					if(Arrays.asList(urlBlockMeta.getNblUrl().split(";")).stream().filter(nblockUrl -> nblockUrl.contains("*") == true).anyMatch(nblockUrl -> checkUrlPattern(nblockUrl, url))){
						return null;
					}
				}

				log.debug("진입URL [{}] : 금일차단, Type : {}, {}~{}", url, urlBlockMeta.getBlType(), startDtm, endDtm);
				if (StringUtils.isNotEmpty(urlBlockMeta.getGuideUrl())) {
					return urlBlockMeta.getGuideUrl();
				}

				if (StringUtils.isNotEmpty(startDtm) && StringUtils.isNotEmpty(endDtm)
					&& startDtm.length() == 12 && endDtm.length() == 12) {
					String[] args = {
						  startDtm.substring(4, 6).replaceFirst("^0+(?!$)", "") // 차단시작 월(앞자리 0제거)
						, startDtm.substring(6, 8).replaceFirst("^0+(?!$)", "") // 차단시작 일(앞자리 0제거)
						, startDtm.substring(8, 10), startDtm.substring(10, 12) // 차단시작 시, 분
						, endDtm.substring(4, 6).replaceFirst("^0+(?!$)", "")   // 차단종료 월(앞자리 0제거)
						, endDtm.substring(6, 8).replaceFirst("^0+(?!$)", "")   // 차단종료 일(앞자리 0제거)
						, endDtm.substring(8, 10), endDtm.substring(10, 12)     // 차단종료 시, 분
					};
					if(checkUrlPattern("/dsds/**", url)) {
						throw new BizException(CommonError.BLOCKED_URL_TIME_DSDS, args);
					} else if("*".equals(blockedUrl)){
						throw new BizException(CommonError.BLOCKED_URL_TIME_ALL, args);
					} else {
						throw new BizException(CommonError.BLOCKED_URL_TIME_ERROR, args);
					}
				} else {
					throw new BizException(CommonError.BLOCKED_URL_ERROR);
				}
			}
		}

		return null;
	}

	private boolean checkUrlPattern(String nblockUrl, String url) {
		log.debug("nBlockUrl : {}, Current URL : {}, Result : {}", nblockUrl , url, parse(nblockUrl).matches(PathContainer.parsePath(url)));
		return parse(nblockUrl).matches(PathContainer.parsePath(url));
	}

	private PathPattern parse(String path) {
		PathPatternParser pp = new PathPatternParser();
		pp.setMatchOptionalTrailingSeparator(true);
		return pp.parse(path);
	}


	private boolean isBlockedParameterValue(String passParamName, String passParamValues, Map<String,String[]> parameterMap) {
		List<String> paramValueList = Arrays.asList(parameterMap.get(passParamName));
		List<String> passParamValueList = Arrays.asList(passParamValues.split(";"));

		return paramValueList.stream().anyMatch(passParamValueList::contains);
	}

	private String getCurrentTime() {
		String DATE_FORMAT = "yyyyMMddHHmm"; // "yyyyMMddHHmmss"
		final int millisPerHour = 60 * 60 * 1000;

		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		SimpleTimeZone timeZone = new SimpleTimeZone(9 * millisPerHour, "KST");
		sdf.setTimeZone(timeZone);

		long time = System.currentTimeMillis();
		Date date = new Date(time);

		return sdf.format(date);
	}

	@Component
	public class BlockUrlRedisDao {
		/**
		 * <ul>
		 * <li>업무명 : URL 차단목록</li>
		 * <li>설 명 : 관리자가 등록한 차단정보 목록을 조회한다.</li>
		 * <li>작성일 : 2018. 9. 19.</li>
		 * <li>작성자 : P127602</li>
		 * <li>
		 * @return List<UrlBlockMeta>
		 * </li>
		 * </ul>
		 */
		public List<String> getUrlBlockCodeList() {
			String urlBlockCodes = CacheUtils.getCacheData(CacheConstants.REDIS_CACHE.UrlBlockMeta + META_SUBKEY_BLOCK_M_BLCODE, FIELD_ID_URL_BLOCK_CODE, String.class);
			if (StringUtils.isNotEmpty(urlBlockCodes)) {
				return Arrays.asList(urlBlockCodes.split(";"));
			} else {
				return null;
			}
		}

		/**
		 * <ul>
		 * <li>업무명 : URL 차단정보</li>
		 * <li>설 명 : 관리자가 등록한 차단정보를 조회한다.</li>
		 * <li>작성일 : 2018. 9. 19.</li>
		 * <li>작성자 : P127602</li>
		 * <li>
		 * @param blockCode
		 * </li>
		 * <li>
		 * @return UrlBlockMeta
		 * </li>
		 * </ul>
		 */
		public UrlBlockMeta getUrlBlockMeta(String blockCode) {
//			return CacheUtils.getCacheData(REDIS_CACHE.UrlBlockMeta + ":block." + blockCode, FIELD_ID_BLOCK_INFO, UrlBlockMeta.class);
			// todo: 패키지를 찾아서 넣고 주석을 풀어주세요.
			return null;
		}

		/**
		 * <ul>
		 * <li>업무명 : 우회 ID정보</li>
		 * <li>설 명 : 관리자가 등록한 차단정보 예외처리 ID를 조회한다.</li>
		 * <li>작성일 : 2018. 12. 14.</li>
		 * <li>작성자 : P128161</li>
		 * <li>
		 * </li>
		 * <li>
		 * @return UrlBlockPassId
		 * </li>
		 * </ul>
		 */
		public UrlBlockPassId getUrlBlockPassId() {
			return CacheUtils.getCacheData(CacheConstants.REDIS_CACHE.UrlBlockMeta + META_SUBKEY_BLOCK_PASS_ID, FIELD_ID_BLOCK_INFO, UrlBlockPassId.class);
		}

		/**
		 * <ul>
		 * <li>업무명 : 우회 Key정보</li>
		 * <li>설 명 : 관리자가 등록한 차단정보 예외처리 Key를 조회한다.</li>
		 * <li>작성일 : 2019. 07. 20.</li>
		 * <li>작성자 : P028874</li>
		 * <li>
		 * </li>
		 * <li>
		 * @return UrlBlockPassId
		 * </li>
		 * </ul>
		 */
		public UrlBlockPassKey getUrlBlockPassKey() {
			return CacheUtils.getCacheData(CacheConstants.REDIS_CACHE.UrlBlockMeta + META_SUBKEY_BLOCK_PASS_KEY, FIELD_ID_BLOCK_INFO, UrlBlockPassKey.class);
		}
	}
}
