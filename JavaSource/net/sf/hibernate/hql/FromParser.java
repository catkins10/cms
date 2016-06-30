//$Id: FromParser.java,v 1.20 2004/06/04 01:27:39 steveebersole Exp $
package net.sf.hibernate.hql;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.hibernate.QueryException;
import net.sf.hibernate.persister.Queryable;
import net.sf.hibernate.sql.JoinFragment;
import net.sf.hibernate.util.StringHelper;

/**
 * Parses the from clause of a hibernate query, looking for tables and
 * aliases for the SQL query.
 */

public class FromParser implements Parser {
	
	private final PathExpressionParser peParser = new FromPathExpressionParser();
	private String entityName;
	private String alias;
	private boolean afterIn;
	private boolean afterAs;
	private boolean afterClass;
	private boolean expectingJoin;
	private boolean expectingIn;
	private boolean expectingAs;
	private boolean afterJoinType;
	private int joinType;
	private boolean afterFetch;

	//add by linchuan
	private int afterOn;
	private static final ThreadLocal threadLocal = new ThreadLocal();
	private String lastEntityName;
	private String mainEntityClassName; //主表类名称
	
	private static final int NONE = -666;
	
	private static final Map JOIN_TYPES = new HashMap();
	static {
		JOIN_TYPES.put( "left", new Integer(JoinFragment.LEFT_OUTER_JOIN) );
		JOIN_TYPES.put( "right", new Integer(JoinFragment.RIGHT_OUTER_JOIN) );
		JOIN_TYPES.put( "full", new Integer(JoinFragment.FULL_JOIN) );
		JOIN_TYPES.put( "inner", new Integer(JoinFragment.INNER_JOIN) );
	}
	
	public void token(String token, QueryTranslator q) throws QueryException {
		// start by looking for HQL keywords...
		String lcToken = token.toLowerCase();
		if (afterOn<=0 && lcToken.equals(StringHelper.COMMA) ) { //逗号
			if ( !(expectingJoin|expectingAs) ) throw new QueryException("unexpected token: ,");
			expectingJoin = false;
			expectingAs = false;
			afterOn = -1;
		}
		else if (afterOn<=0 && lcToken.equals("join") ) {
			if (!afterJoinType) {
				if ( !(expectingJoin|expectingAs) ) throw new QueryException("unexpected token: join");
				// inner joins can be abbreviated to 'join'
				joinType = JoinFragment.INNER_JOIN;
				expectingJoin = false;
				expectingAs = false;
			}
			else {
				afterJoinType = false;
			}
			afterOn = -1;
		}
		else if (afterOn<=0 && lcToken.equals("fetch") ) {
			if ( q.isShallowQuery() ) throw new QueryException("fetch may not be used with scroll() or iterate()");
			if (joinType==NONE) throw new QueryException("unexpected token: fetch");
			if (joinType==JoinFragment.FULL_JOIN || joinType==JoinFragment.RIGHT_OUTER_JOIN) 
				throw new QueryException("fetch may only be used with inner join or left outer join");
			afterFetch = true;
			afterOn = -1;
		}
		else if (afterOn<=0 && lcToken.equals("outer") ) {
			afterOn = -1;
			// 'outer' is optional and is ignored
			if ( !afterJoinType || 
				(joinType!=JoinFragment.LEFT_OUTER_JOIN && joinType!=JoinFragment.RIGHT_OUTER_JOIN) 
			) throw new QueryException("unexpected token: outer");
		}
		else if (afterOn<=0 && JOIN_TYPES.containsKey(lcToken) ) {
			if ( !(expectingJoin|expectingAs) ) throw new QueryException("unexpected token: " + token);
			joinType = ( (Integer) JOIN_TYPES.get(lcToken) ).intValue();
			afterJoinType = true;
			expectingJoin = false;
			expectingAs = false;
			afterOn = -1;
		}
		else if(afterOn!=-1) {
			if(token.equals("(")) {
				afterOn++;
			}
			else if(token.equals(")")) {
				afterOn--;
			}
			Map hqlOns = (Map)threadLocal.get();
			StringBuffer hqlOn = (StringBuffer)hqlOns.get(lastEntityName);
			if(hqlOn==null) {
				hqlOn = new StringBuffer();
				hqlOns.put(lastEntityName, hqlOn);
			}
			hqlOn.append(' ');
			hqlOn.append(token);
		}
		else if (lcToken.equals("class") ) {
			if (!afterIn) throw new QueryException("unexpected token: class");
			if (joinType!=NONE) throw new QueryException("outer or full join must be followed by path expression");
			afterClass=true;
		}
		else if ( lcToken.equals("in") ) {
			if (!expectingIn) throw new QueryException("unexpected token: in");
			afterIn = true;
			expectingIn = false;
		}
		else if ( lcToken.equals("as") ) {
			if (!expectingAs) throw new QueryException("unexpected token: as");
			afterAs = true;
			expectingAs = false;
		}
		else if ( lcToken.equals("on") ) {
			afterOn = 0;
		}
		else {
			
			if (afterJoinType) throw new QueryException("join expected: " + token);
			if (expectingJoin) throw new QueryException("unexpected token: " + token);
			if (expectingIn) throw new QueryException("in expected: " + token);
			
			// now anything that is not a HQL keyword
			
			if ( afterAs || expectingAs ) { 
				
				// (AS is always optional, for consistency with SQL/OQL)
				
				// process the "new" HQL style where aliases are assigned 
				// _after_ the class name or path expression ie. using
				// the AS construction
				
				if (entityName!=null) {
					q.setAliasName(token, entityName);
					lastEntityName = entityName;
				}
				else {
					throw new QueryException("unexpected: as " + token);
				}
				afterAs = false;
				expectingJoin = true;
				expectingAs = false;
				entityName = null;
				
			}
			else if (afterIn) {
				
				// process the "old" HQL style where aliases appear _first_
				// ie. using the IN or IN CLASS constructions
				
				if (alias==null) throw new QueryException("alias not specified for: " + token);
				
				if (joinType!=NONE) throw new QueryException("outer or full join must be followed by path expression");
				
				if (afterClass) {
					// treat it as a classname
					Queryable p = q.getPersisterUsingImports(token);
					if (p==null) throw new QueryException("persister not found: " + token);
					q.addFromClass(alias, p);
				}
				else {
					// treat it as a path expression
					peParser.setJoinType(JoinFragment.INNER_JOIN);
					peParser.setUseThetaStyleJoin(true);
					ParserHelper.parse(peParser, q.unalias(token), ParserHelper.PATH_SEPARATORS, q);
					if ( !peParser.isCollectionValued() ) throw new QueryException("path expression did not resolve to collection: " + token);
					String nm = peParser.addFromCollection(q);
					q.setAliasName(alias, nm);
				}
				
				alias = null;
				afterIn = false;
				afterClass = false;
				expectingJoin = true;
			}
			else {
				
				// handle a path expression or class name that
				// appears at the start, in the "new" HQL
				// style or an alias that appears at the start
				// in the "old" HQL style
				
				Queryable p = q.getPersisterUsingImports(token);
				if (p!=null) {
					mainEntityClassName = token;
					// starts with the name of a mapped class (new style)
					if (joinType!=NONE) throw new QueryException("outer or full join must be followed by path expression");
					entityName = q.createNameFor( p.getMappedClass() );
					q.addFromClass(entityName, p);
					expectingAs = true;
				}
				else if ( token.indexOf('.') < 0 ) {
					// starts with an alias (old style)
					// semi-bad thing about this: can't re-alias another alias.....
					alias = token;
					expectingIn = true;
				}
				else {
					
					// starts with a path expression (new style)
					
					// force HQL style: from Person p inner join p.cars c
					//if (joinType==NONE) throw new QueryException("path expression must be preceded by full, left, right or inner join");
					
					//allow ODMG OQL style: from Person p, p.cars c
					if (joinType!=NONE) {
						peParser.setJoinType(joinType);
					}
					else {
						peParser.setJoinType(JoinFragment.INNER_JOIN);
					}
					peParser.setUseThetaStyleJoin( q.isSubquery() );
					
					ParserHelper.parse(peParser, q.unalias(token), ParserHelper.PATH_SEPARATORS, q);
					entityName = peParser.addFromAssociation(q);
					
					joinType = NONE;
					peParser.setJoinType(JoinFragment.INNER_JOIN);
					
					if (afterFetch) {
						peParser.fetch(q, entityName);
						afterFetch = false;
					}
					
					expectingAs = true;

				}
			}
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.hibernate.hql.Parser#start(net.sf.hibernate.hql.QueryTranslator)
	 */
	public void start(QueryTranslator q) {
		entityName = null;
		alias = null;
		afterIn = false;
		afterAs = false;
		afterClass = false;
		expectingJoin = false;
		expectingIn = false;
		expectingAs = false;
		joinType = NONE;

		afterOn = -1;
		if(!q.isSubquery()) {
			Map hqlOns = (Map)threadLocal.get();
			if(hqlOns!=null) {
				hqlOns.clear();
			}
			else {
				hqlOns = new HashMap();
				threadLocal.set(hqlOns);
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.hibernate.hql.Parser#end(net.sf.hibernate.hql.QueryTranslator)
	 */
	public void end(QueryTranslator q) {
		Map hqlOns = (Map)threadLocal.get();
		Set keys = new HashSet();
		keys.addAll(hqlOns.keySet());
		for(Iterator iterator = keys.iterator(); iterator.hasNext();) {
			String key = (String)iterator.next();
			Object value = hqlOns.get(key);
			if(value==null || !(value instanceof StringBuffer)) {
				continue;
			}
			StringBuffer hqlOn = (StringBuffer)value;
			hqlOns.put(key, null);
			QueryTranslator subQuery = new QueryTranslator("select temp2005.id from " + mainEntityClassName + " temp2005 where " + hqlOn);
			try {
				subQuery.compile(q);
			}
			catch (Exception e) {
				throw new Error("MappingException occurred compiling on sql", e);
			}
			String sql = subQuery.getSQLString();
			hqlOns.put(key, sql.substring(sql.indexOf(" where ") + " where ".length()));
		}
	}
	
	/**
	 * 重置ON语句
	 * @param from
	 * @return
	 */
	public static String retrieveFromSql(String from) {
		Map hqlOns = (Map)threadLocal.get();
		for(Iterator iterator = hqlOns==null ? null : hqlOns.keySet().iterator(); iterator!=null && iterator.hasNext();) {
			String alias = (String)iterator.next();
			Object value = hqlOns.get(alias);
			if(value==null || !(alias instanceof String)) {
				continue;
			}
			from = from.replaceFirst("( " + alias + " on )[^ ,\r\n]*([ ,\r\n]?)", "$1" + value + "$2");
		}
		return from;
	}
}
