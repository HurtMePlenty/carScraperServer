/*
 * Copyright 1996-2002 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package org.silvertunnel_ng.netlib.adapter.url.impl.net.http;

import java.util.Iterator;

/* This is useful for the nightmare of parsing multi-part HTTP/RFC822 headers
 * sensibly:
 * From a String like: 'timeout=15, max=5'
 * create an array of Strings:
 * { {"timeout", "15"},
 *   {"max", "5"}
 * }
 * From one like: 'Basic Realm="FuzzFace" Foo="Biz Bar Baz"'
 * create one like (no quotes in literal):
 * { {"basic", null},
 *   {"realm", "FuzzFace"}
 *   {"foo", "Biz Bar Baz"}
 * }
 * keys are converted to lower case, vals are left as is....
 *
 * @author Dave Brown
 */

public class HeaderParser
{

	/* table of key/val pairs */
	String raw;
	String[][] tab;
	int nkeys;
	int asize = 10; // initial size of array is 10

	public HeaderParser(String raw)
	{
		this.raw = raw;
		tab = new String[asize][2];
		parse();
	}

	private HeaderParser()
	{
	}

	/**
	 * create a new HeaderParser from this, whose keys (and corresponding
	 * values) range from "start" to "end-1"
	 */
	public HeaderParser subsequence(int start, int end)
	{
		if (start == 0 && end == nkeys)
		{
			return this;
		}
		if (start < 0 || start >= end || end > nkeys)
		{
			throw new IllegalArgumentException("invalid start or end");
		}
		final HeaderParser n = new HeaderParser();
		n.tab = new String[asize][2];
		n.asize = asize;
		System.arraycopy(tab, start, n.tab, 0, (end - start));
		n.nkeys = (end - start);
		return n;
	}

	private void parse()
	{

		if (raw != null)
		{
			raw = raw.trim();
			final char[] ca = raw.toCharArray();
			int beg = 0, end = 0, i = 0;
			boolean inKey = true;
			boolean inQuote = false;
			final int len = ca.length;
			while (end < len)
			{
				final char c = ca[end];
				if ((c == '=') && !inQuote)
				{ // end of a key
					tab[i][0] = new String(ca, beg, end - beg).toLowerCase();
					inKey = false;
					end++;
					beg = end;
				}
				else if (c == '\"')
				{
					if (inQuote)
					{
						tab[i++][1] = new String(ca, beg, end - beg);
						inQuote = false;
						do
						{
							end++;
						}
						while (end < len && (ca[end] == ' ' || ca[end] == ','));
						inKey = true;
						beg = end;
					}
					else
					{
						inQuote = true;
						end++;
						beg = end;
					}
				}
				else if (c == ' ' || c == ',')
				{ // end key/val, of whatever we're in
					if (inQuote)
					{
						end++;
						continue;
					}
					else if (inKey)
					{
						tab[i++][0] = (new String(ca, beg, end - beg))
								.toLowerCase();
					}
					else
					{
						tab[i++][1] = (new String(ca, beg, end - beg));
					}
					while (end < len && (ca[end] == ' ' || ca[end] == ','))
					{
						end++;
					}
					inKey = true;
					beg = end;
				}
				else
				{
					end++;
				}
				if (i == asize)
				{
					asize = asize * 2;
					final String[][] ntab = new String[asize][2];
					System.arraycopy(tab, 0, ntab, 0, tab.length);
					tab = ntab;
				}
			}
			// get last key/val, if any
			if (--end > beg)
			{
				if (!inKey)
				{
					if (ca[end] == '\"')
					{
						tab[i++][1] = (new String(ca, beg, end - beg));
					}
					else
					{
						tab[i++][1] = (new String(ca, beg, end - beg + 1));
					}
				}
				else
				{
					tab[i++][0] = (new String(ca, beg, end - beg + 1))
							.toLowerCase();
				}
			}
			else if (end == beg)
			{
				if (!inKey)
				{
					if (ca[end] == '\"')
					{
						tab[i++][1] = String.valueOf(ca[end - 1]);
					}
					else
					{
						tab[i++][1] = String.valueOf(ca[end]);
					}
				}
				else
				{
					tab[i++][0] = String.valueOf(ca[end]).toLowerCase();
				}
			}
			nkeys = i;
		}

	}

	public String findKey(int i)
	{
		if (i < 0 || i > asize)
		{
			return null;
		}
		return tab[i][0];
	}

	public String findValue(int i)
	{
		if (i < 0 || i > asize)
		{
			return null;
		}
		return tab[i][1];
	}

	public String findValue(String key)
	{
		return findValue(key, null);
	}

	public String findValue(String key, String defaultValue)
	{
		if (key == null)
		{
			return defaultValue;
		}
		key = key.toLowerCase();
		for (int i = 0; i < asize; ++i)
		{
			if (tab[i][0] == null)
			{
				return defaultValue;
			}
			else if (key.equals(tab[i][0]))
			{
				return tab[i][1];
			}
		}
		return defaultValue;
	}

	class ParserIterator implements Iterator<Object>
	{
		private int index;
		private boolean returnsValue; // or key

		ParserIterator(final boolean returnValue)
		{
			returnsValue = returnValue;
		}

		@Override
		public boolean hasNext()
		{
			return index < nkeys;
		}

		@Override
		public Object next()
		{
			return tab[index++][returnsValue ? 1 : 0];
		}

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException("remove not supported");
		}
	}

	public Iterator<Object> keys()
	{
		return new ParserIterator(false);
	}

	public Iterator<Object> values()
	{
		return new ParserIterator(true);
	}

	@Override
	public String toString()
	{
		final Iterator<Object> k = keys();
		final StringBuffer sbuf = new StringBuffer();
		sbuf.append("{size=" + asize + " nkeys=" + nkeys + " ");
		for (int i = 0; k.hasNext(); i++)
		{
			final String key = (String) k.next();
			String val = findValue(i);
			if (val != null && "".equals(val))
			{
				val = null;
			}
			sbuf.append(" {" + key + (val == null ? "" : "," + val) + "}");
			if (k.hasNext())
			{
				sbuf.append(",");
			}
		}
		sbuf.append(" }");
		return new String(sbuf);
	}

	public int findInt(final String k, final int Default)
	{
		try
		{
			return Integer.parseInt(findValue(k, String.valueOf(Default)));
		}
		catch (final Throwable t)
		{
			return Default;
		}
	}
	/*
	 * public static void main(String[] a) throws Exception {
	 * System.out.print("enter line to parse> "); System.out.flush();
	 * DataInputStream dis = new DataInputStream(System.in); String line =
	 * dis.readLine(); HeaderParser p = new HeaderParser(line); for (int i = 0;
	 * i < asize; ++i) { if (p.findKey(i) == null) break; String v =
	 * p.findValue(i); LOG.info(i + ") " +p.findKey(i) + "="+v); }
	 * LOG.info("Done!");
	 * 
	 * }
	 */
}
